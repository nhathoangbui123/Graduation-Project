#include "aws.h"

#include "aws_iot_config.h"
#include "aws_iot_log.h"
#include "aws_iot_version.h"
#include "aws_iot_mqtt_client_interface.h"

#include "esp_log.h"
#include "freertos/FreeRTOS.h"
#include "freertos/task.h"

#include "cJSON.h"
#include <ctype.h>
#include <string.h>

#include "./aws/certs/cert.h"
#include "config.h"
#include "cmd.h"

static const char * TAG = "AWS";

char Stag_Endpoint[255] = "a3suuuxay09k3c-ats.iot.us-east-2.amazonaws.com";

uint32_t port = 8883;

void iot_subscribe_callback_handler(AWS_IoT_Client *pClient, char *topicName, uint16_t topicNameLen,
                                            IoT_Publish_Message_Params *params, void *pData){
    ESP_LOGI(TAG, "Subscribe callback");
    ESP_LOGI(TAG, "%.*s\t%.*s", topicNameLen, topicName, (int) params->payloadLen, (char *)params->payload);
    cJSON *sub;
    sub=cJSON_Parse((char *)params->payload);

    cJSON *body =cJSON_GetObjectItem(sub,"body");
    char *value_body;
    if(body){
        value_body=cJSON_GetObjectItem(sub,"body")->valuestring;
        ESP_LOGI(TAG, "body is %s",value_body);
    }else{
        value_body="";
    }
    cJSON *command =cJSON_GetObjectItem(sub,"command");
    if(command){
        char *value_type_cmd =cJSON_GetObjectItem(sub,"command")->valuestring;
        ESP_LOGI(TAG, "command is %s",value_type_cmd);
        ParseCmd(value_type_cmd, value_body);
    }
    cJSON_Delete(sub);
}
void disconnectCallbackHandler(AWS_IoT_Client *pClient, void *data){
    ESP_LOGW(TAG, "MQTT Disconnect");
    IoT_Error_t rc = FAILURE;

    if(NULL == pClient) {
        return;
    }

    if(aws_iot_is_autoreconnect_enabled(pClient)) {
        ESP_LOGI(TAG, "Auto Reconnect is enabled, Reconnecting attempt will start now");
    } else {
        ESP_LOGW(TAG, "Auto Reconnect not enabled. Starting manual reconnect...");
        rc = aws_iot_mqtt_attempt_reconnect(pClient);
        if(NETWORK_RECONNECTED == rc) {
            ESP_LOGW(TAG, "Manual Reconnect Successful");
        } else {
            ESP_LOGW(TAG, "Manual Reconnect Failed - %d", rc);
        }
    }
}
void aws_iot_task(void *arg){

    IoT_Error_t rc = FAILURE;
    
    AWS_IoT_Client client;
    IoT_Client_Init_Params mqttInitParams = iotClientInitParamsDefault;
    IoT_Client_Connect_Params connectParams = iotClientConnectParamsDefault;

    IoT_Publish_Message_Params paramsQOS0;

    ESP_LOGI(TAG, "AWS IoT SDK Version %d.%d.%d-%s", VERSION_MAJOR, VERSION_MINOR, VERSION_PATCH, VERSION_TAG);

    mqttInitParams.enableAutoReconnect = false; // We enable this later below
    mqttInitParams.pHostURL = Stag_Endpoint;
    mqttInitParams.port = port;

    mqttInitParams.pRootCALocation = ROOT_CA;
    mqttInitParams.pDeviceCertLocation = DEVICE_CERTIFICATE_MINI2;
    mqttInitParams.pDevicePrivateKeyLocation = DEVICE_PRIVATE_KEY_MINI2;


    mqttInitParams.mqttCommandTimeout_ms = 20000;
    mqttInitParams.tlsHandshakeTimeout_ms = 5000;
    mqttInitParams.isSSLHostnameVerify = true;
    mqttInitParams.disconnectHandler = disconnectCallbackHandler;
    mqttInitParams.disconnectHandlerData = NULL;

    rc = aws_iot_mqtt_init(&client, &mqttInitParams);
    if(SUCCESS != rc) {
        ESP_LOGE(TAG, "aws_iot_mqtt_init returned error : %d ", rc);
    }

    connectParams.keepAliveIntervalInSec = 10;
    connectParams.isCleanSession = true;
    connectParams.MQTTVersion = MQTT_3_1_1;

    connectParams.pClientID = Client_ID;
    connectParams.clientIDLen = (uint16_t) strlen(Client_ID);
    connectParams.isWillMsgPresent = false;

    ESP_LOGI(TAG, "Connecting to AWS...");
    
    do {
        rc = aws_iot_mqtt_connect(&client, &connectParams);
        if(SUCCESS != rc) {
            ESP_LOGE(TAG, "Error(%d) connecting to %s:%d", rc, mqttInitParams.pHostURL, mqttInitParams.port);
            vTaskDelay(500 / portTICK_RATE_MS);
        }
    } while(SUCCESS != rc);

    ESP_LOGI(TAG, "Connected to AWS!");
    rc = aws_iot_mqtt_autoreconnect_set_status(&client, true);
    if(SUCCESS != rc) {
        ESP_LOGE(TAG, "Unable to set Auto Reconnect to true - %d", rc);
    }

    const char *TOPIC_PUB = Topic_Pub;
    const int TOPIC_PUB_LEN = strlen(TOPIC_PUB);

    const char *TOPIC_SUB = Topic_Sub;
    const int TOPIC_SUB_LEN = strlen(TOPIC_SUB);
    
    ESP_LOGI(TAG, "Subscribing...");
    rc = aws_iot_mqtt_subscribe(&client, TOPIC_SUB, TOPIC_SUB_LEN, QOS0, iot_subscribe_callback_handler, NULL);
    if(SUCCESS != rc) {
        ESP_LOGE(TAG, "Error subscribing : %d ", rc);
    }
    
    while((NETWORK_ATTEMPTING_RECONNECT == rc || NETWORK_RECONNECTED == rc || SUCCESS == rc)) {

        //Max time the yield function will wait for read messages
        rc = aws_iot_mqtt_yield(&client, 100);
        if(NETWORK_ATTEMPTING_RECONNECT == rc) {
            // If the client is attempting to reconnect we will skip the rest of the loop.
            continue;
        }
        ESP_LOGI(TAG, "Stack remaining for task '%s' is %d bytes", pcTaskGetTaskName(NULL), uxTaskGetStackHighWaterMark(NULL));
        ESP_LOGI(TAG, "[APP] Free memory: %d bytes", esp_get_free_heap_size());
        cJSON *pub;
        char *data_json;
        int length_data_json;
        pub = cJSON_CreateObject();
        cJSON_AddStringToObject(pub, "MAC", "01:02:03:04:05:06");

        //Total
        //cJSON_AddNumberToObject(pub, "P", param.power);
        //cJSON_AddNumberToObject(pub, "E", param.energy);
        cJSON_AddNumberToObject(pub, "F", param.frequency);
        //cJSON_AddNumberToObject(pub, "I", param.current);
        cJSON_AddNumberToObject(pub, "U", param.voltage);
        //cJSON_AddNumberToObject(pub, "C", param.cost);

        //User1
        cJSON_AddNumberToObject(pub, "EU1", param.energy1);
        cJSON_AddNumberToObject(pub, "IU1", param.current1);
        cJSON_AddNumberToObject(pub, "CU1", param.cost1);

        //User2
        cJSON_AddNumberToObject(pub, "EU2", param.energy2);
        cJSON_AddNumberToObject(pub, "IU2", param.current2);
        cJSON_AddNumberToObject(pub, "CU2", param.cost2);

        //Device cost
        cJSON_AddNumberToObject(pub, "C1",param.C1);
        cJSON_AddNumberToObject(pub, "C2",param.C2);
        cJSON_AddNumberToObject(pub, "C3",param.C3);
        cJSON_AddNumberToObject(pub, "C4",param.C4);

        //Device current
        cJSON_AddNumberToObject(pub, "I1",param.I1);
        cJSON_AddNumberToObject(pub, "I2",param.I2);
        cJSON_AddNumberToObject(pub, "I3",param.I3);
        cJSON_AddNumberToObject(pub, "I4",param.I4);

        //Device Energy
        cJSON_AddNumberToObject(pub, "E1",param.E1);
        cJSON_AddNumberToObject(pub, "E2",param.E2);
        cJSON_AddNumberToObject(pub, "E3",param.E3);
        cJSON_AddNumberToObject(pub, "E4",param.E4);

        //Device power
        cJSON_AddNumberToObject(pub, "P1",param.P1);
        cJSON_AddNumberToObject(pub, "P2",param.P2);
        cJSON_AddNumberToObject(pub, "P3",param.P3);
        cJSON_AddNumberToObject(pub, "P4",param.P4);

        //Device threshold
        cJSON_AddNumberToObject(pub, "T1",param.T1);
        cJSON_AddNumberToObject(pub, "T2",param.T2);
        cJSON_AddNumberToObject(pub, "T3",param.T3);
        cJSON_AddNumberToObject(pub, "T4",param.T4);

        //Threshold flag
        cJSON_AddNumberToObject(pub, "T1F",param.T1F);
        cJSON_AddNumberToObject(pub, "T2F",param.T2F);
        cJSON_AddNumberToObject(pub, "T3F",param.T3F);
        cJSON_AddNumberToObject(pub, "T4F",param.T4F);

        //Settings
        cJSON_AddNumberToObject(pub, "EP",param.EP);
        cJSON_AddStringToObject(pub, "WN",param.WN);
        cJSON_AddStringToObject(pub, "WP",param.WP);

        //Device status
        cJSON_AddNumberToObject(pub, "Sdev1", device.dev1_state);
        cJSON_AddNumberToObject(pub, "Sdev2", device.dev2_state);
        cJSON_AddNumberToObject(pub, "Sdev3", device.dev3_state);
        cJSON_AddNumberToObject(pub, "Sdev4", device.dev4_state);
        
        memset(&data_json,0,sizeof(data_json));
        data_json = cJSON_Print(pub);
        length_data_json=strlen(data_json);
        ESP_LOGI(TAG, "data serialized %s", data_json);
        ESP_LOGI(TAG, "strlen(data) %d", length_data_json);
        paramsQOS0.qos = QOS0;
        paramsQOS0.isRetained = 0;
        paramsQOS0.payload =data_json;
        paramsQOS0.payloadLen = length_data_json;
        
        rc = aws_iot_mqtt_publish(&client, TOPIC_PUB, TOPIC_PUB_LEN, &paramsQOS0);
        if (rc == MQTT_REQUEST_TIMEOUT_ERROR) {
            ESP_LOGW(TAG, "QOS0 publish ack not received.");
            rc = SUCCESS;
        }
        free(data_json);
        cJSON_Delete(pub);
        vTaskDelay(500 / portTICK_RATE_MS);
    }
    ESP_LOGE(TAG, "An error occurred in the main loop.");
}
