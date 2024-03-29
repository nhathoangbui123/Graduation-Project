
#include "nextion.h"
#include "config.h"
#include "cJSON.h"

void initNextion() {
    const uart_config_t uart_config = {
        .baud_rate = 9600,
        .data_bits = UART_DATA_8_BITS,
        .parity = UART_PARITY_DISABLE,
        .stop_bits = UART_STOP_BITS_1,
        .flow_ctrl = UART_HW_FLOWCTRL_DISABLE
    };
    uart_param_config(nUART, &uart_config);
    uart_set_pin(nUART, TX_HMI, RX_HMI, UART_PIN_NO_CHANGE, UART_PIN_NO_CHANGE);
    // We won't use a buffer for sending data.
    uart_driver_install(nUART, RX_BUFFER * 2, 0, 0, NULL, 0);
}

int sendData(const char* logName, const char* data)
{
    const int len = strlen(data);
    const int txBytes = uart_write_bytes(nUART, data, len);
    ESP_LOGD(logName, "Wrote %d bytes", txBytes);
    ESP_LOGD(logName, "Sent %s", data);
    return txBytes;
}

void tx_task(void* arg)
{
  ESP_LOGD(TX_TASK_TAG,"NEXTION TX TASK");
  char *cmd = (char*)malloc(TX_BUFFER);
  while (1) {
    ESP_LOGD(TX_TASK_TAG, "[APP] Free memory: %d bytes", esp_get_free_heap_size());
    
    sprintf(cmd, "Monitor.power_v.txt=\"%0.3fKWh\"\xFF\xFF\xFF",param.energy);
    ESP_LOGD(TX_TASK_TAG, "Monitor.power_v.txt=%0.3fKWh", param.energy);
    sendData(TX_TASK_TAG, cmd);	

    sprintf(cmd, "Monitor.freq_v.txt=\"%0.1fHz\"\xFF\xFF\xFF",param.frequency);
    ESP_LOGD(TX_TASK_TAG, "Monitor.freq_v.txt=%0.1fHz", param.frequency);
    sendData(TX_TASK_TAG, cmd);

    sprintf(cmd, "Monitor.vol_v.txt=\"%0.1fV\"\xFF\xFF\xFF",param.voltage);
    ESP_LOGD(TX_TASK_TAG, "Monitor.vol_v.txt=%0.1fV", param.voltage);
    sendData(TX_TASK_TAG, cmd);

    sprintf(cmd, "Monitor.amp_v.txt=\"%0.2fA\"\xFF\xFF\xFF",param.current);
    ESP_LOGD(TX_TASK_TAG, "Monitor.amp_v.txt=%0.2fA", param.current);
    sendData(TX_TASK_TAG, cmd);

    //electric price and total cost
    sprintf(cmd, "Check.vndPerKwh.txt=\"%d VND/kWh\"\xFF\xFF\xFF",param.EP);
    ESP_LOGD(TX_TASK_TAG, "Check.vndPerKwh.txt=%d VND/kWh", param.EP);
    sendData(TX_TASK_TAG, cmd);

    sprintf(cmd, "Check.t0.txt=\"%0.1f VND\"\xFF\xFF\xFF",param.cost);
    ESP_LOGD(TX_TASK_TAG, "Check.t0.txt=%0.1f VND", param.cost);
    sendData(TX_TASK_TAG, cmd);

    sprintf(cmd, "Check.t5.txt=\"%0.1f VND\"\xFF\xFF\xFF",param.cost1);
    ESP_LOGD(TX_TASK_TAG, "Check.t5.txt=%0.1f VND", param.cost1);
    sendData(TX_TASK_TAG, cmd);

    sprintf(cmd, "Check.t3.txt=\"%0.1f VND\"\xFF\xFF\xFF",param.cost2);
    ESP_LOGD(TX_TASK_TAG, "Check.t3.txt=%0.1f VND", param.cost2);
    sendData(TX_TASK_TAG, cmd);

    //wifi name and wifi password
    sprintf(cmd, "Setting.wifi.txt=\"%s\"\xFF\xFF\xFF",param.WN);
    ESP_LOGD(TX_TASK_TAG, "Setting.wifi.txt=%s", param.WN);
    sendData(TX_TASK_TAG, cmd);

    sprintf(cmd, "Setting.pwd.txt=\"%s\"\xFF\xFF\xFF",param.WP);
    ESP_LOGD(TX_TASK_TAG, "Setting.pwd.txt=%s", param.WP);
    sendData(TX_TASK_TAG, cmd);
    
    //threshold
    sprintf(cmd, "Monitor_Dev1.thr_dev1.txt=\"%d W\"\xFF\xFF\xFF",param.T1);
    ESP_LOGD(TX_TASK_TAG, "Monitor_Dev1.thr_dev1.txt=%d W", param.T1);
    sendData(TX_TASK_TAG, cmd);

    sprintf(cmd, "Monitor_Dev2.thr_dev2.txt=\"%d W\"\xFF\xFF\xFF",param.T2);
    ESP_LOGD(TX_TASK_TAG, "Monitor_Dev2.thr_dev2.txt=%d W", param.T2);
    sendData(TX_TASK_TAG, cmd);

    sprintf(cmd, "Monitor_Dev3.thr_dev3.txt=\"%d W\"\xFF\xFF\xFF",param.T3);
    ESP_LOGD(TX_TASK_TAG, "Monitor_Dev3.thr_dev3.txt=%d W", param.T3);
    sendData(TX_TASK_TAG, cmd);

    sprintf(cmd, "Monitor_Dev4.thr_dev4.txt=\"%d W\"\xFF\xFF\xFF",param.T4);
    ESP_LOGD(TX_TASK_TAG, "Monitor_Dev4.thr_dev4.txt=%d W", param.T4);
    sendData(TX_TASK_TAG, cmd);
    
    if(device.dev1_state){
      sendData(TX_TASK_TAG, "Control.t1.txt=\"ON\"\xFF\xFF\xFF");	
      sendData(TX_TASK_TAG, "Control.t1.pco=2016\xFF\xFF\xFF");	
      sendData(TX_TASK_TAG, "Control.bt0.val=1\xFF\xFF\xFF");

      sprintf(cmd, "Monitor_Dev1.power_dev1.txt=\"%0.2fW\"\xFF\xFF\xFF",param.P[0]);
      ESP_LOGD(TX_TASK_TAG, "Monitor_Dev1.power_dev1.txt=%0.2fW", param.P[0]);
      sendData(TX_TASK_TAG, cmd);	

      sprintf(cmd, "Monitor_Dev1.amp_dev1.txt=\"%0.2fA\"\xFF\xFF\xFF",param.I[0]);
      ESP_LOGD(TX_TASK_TAG, "Monitor_Dev1.amp_dev1.txt=%0.2fA", param.I[0]);
      sendData(TX_TASK_TAG, cmd);

      gpio_set_level(DEVICE_1, ON);	

    }else if(!device.dev1_state){
      sendData(TX_TASK_TAG, "Control.t1.txt=\"OFF\"\xFF\xFF\xFF");
      sendData(TX_TASK_TAG, "Control.t1.pco=63488\xFF\xFF\xFF");
      sendData(TX_TASK_TAG, "Control.bt0.val=0\xFF\xFF\xFF");	
      sendData(TX_TASK_TAG, "Monitor_Dev1.power_dev1.txt=\"0.00W\"\xFF\xFF\xFF");
      sendData(TX_TASK_TAG, "Monitor_Dev1.amp_dev1.txt=\"0.00A\"\xFF\xFF\xFF");

      gpio_set_level(DEVICE_1, OFF);
    }

    if(device.dev2_state){
      sendData(TX_TASK_TAG, "Control.t4.txt=\"ON\"\xFF\xFF\xFF");	
      sendData(TX_TASK_TAG, "Control.t4.pco=2016\xFF\xFF\xFF");	
      sendData(TX_TASK_TAG, "Control.bt1.val=1\xFF\xFF\xFF");

      sprintf(cmd, "Monitor_Dev2.power_dev2.txt=\"%0.2fW\"\xFF\xFF\xFF",param.P[1]);
      ESP_LOGD(TX_TASK_TAG, "Monitor_Dev2.power_dev2.txt=%0.2fW", param.P[1]);
      sendData(TX_TASK_TAG, cmd);	

      sprintf(cmd, "Monitor_Dev2.amp_dev2.txt=\"%0.2fA\"\xFF\xFF\xFF",param.I[1]);
      ESP_LOGD(TX_TASK_TAG, "Monitor_Dev2.amp_dev2.txt=%0.2fA", param.I[1]);
      sendData(TX_TASK_TAG, cmd);	

      gpio_set_level(DEVICE_2, ON);

    }else if(!device.dev2_state){
      sendData(TX_TASK_TAG, "Control.t4.txt=\"OFF\"\xFF\xFF\xFF");
      sendData(TX_TASK_TAG, "Control.t4.pco=63488\xFF\xFF\xFF");	
      sendData(TX_TASK_TAG, "Control.bt1.val=0\xFF\xFF\xFF");

      sendData(TX_TASK_TAG, "Monitor_Dev2.power_dev2.txt=\"0.00W\"\xFF\xFF\xFF");
      sendData(TX_TASK_TAG, "Monitor_Dev2.amp_dev2.txt=\"0.00A\"\xFF\xFF\xFF");

      gpio_set_level(DEVICE_2, OFF);
    }

    if(device.dev3_state){
      sendData(TX_TASK_TAG, "Control.t6.txt=\"ON\"\xFF\xFF\xFF");	
      sendData(TX_TASK_TAG, "Control.t6.pco=2016\xFF\xFF\xFF");	
      sendData(TX_TASK_TAG, "Control.bt2.val=1\xFF\xFF\xFF");

      sprintf(cmd, "Monitor_Dev3.power_dev3.txt=\"%0.2fW\"\xFF\xFF\xFF",param.P[2]);
      ESP_LOGD(TX_TASK_TAG, "Monitor_Dev3.power_dev3.txt=%0.2fW", param.P[2]);
      sendData(TX_TASK_TAG, cmd);	

      sprintf(cmd, "Monitor_Dev3.amp_dev3.txt=\"%0.2fA\"\xFF\xFF\xFF",param.I[2]);
      ESP_LOGD(TX_TASK_TAG, "Monitor_Dev3.amp_dev3.txt=%0.2fA", param.I[2]);
      sendData(TX_TASK_TAG, cmd);	
      gpio_set_level(DEVICE_3, ON);
    }else if(!device.dev3_state){
      sendData(TX_TASK_TAG, "Control.t6.txt=\"OFF\"\xFF\xFF\xFF");
      sendData(TX_TASK_TAG, "Control.t6.pco=63488\xFF\xFF\xFF");	
      sendData(TX_TASK_TAG, "Control.bt2.val=0\xFF\xFF\xFF");

      sendData(TX_TASK_TAG, "Monitor_Dev3.power_dev3.txt=\"0.00W\"\xFF\xFF\xFF");
      sendData(TX_TASK_TAG, "Monitor_Dev3.amp_dev3.txt=\"0.00A\"\xFF\xFF\xFF");
      gpio_set_level(DEVICE_3, OFF);
    }

    if(device.dev4_state){
      sendData(TX_TASK_TAG, "Control.t8.txt=\"ON\"\xFF\xFF\xFF");	
      sendData(TX_TASK_TAG, "Control.t8.pco=2016\xFF\xFF\xFF");	
      sendData(TX_TASK_TAG, "Control.bt3.val=1\xFF\xFF\xFF");

      sprintf(cmd, "Monitor_Dev4.power_dev4.txt=\"%0.2fW\"\xFF\xFF\xFF",param.P[3]);
      ESP_LOGD(TX_TASK_TAG, "Monitor_Dev4.power_dev4.txt=%0.2fW", param.P[3]);
      sendData(TX_TASK_TAG, cmd);	

      sprintf(cmd, "Monitor_Dev4.amp_dev4.txt=\"%0.2fA\"\xFF\xFF\xFF",param.I[3]);
      ESP_LOGD(TX_TASK_TAG, "Monitor_Dev4.amp_dev4.txt=%0.2fA", param.I[3]);
      gpio_set_level(DEVICE_4, ON);
      sendData(TX_TASK_TAG, cmd);	
    }else if(!device.dev4_state){
      sendData(TX_TASK_TAG, "Control.t8.txt=\"OFF\"\xFF\xFF\xFF");
      sendData(TX_TASK_TAG, "Control.t8.pco=63488\xFF\xFF\xFF");	
      sendData(TX_TASK_TAG, "Control.bt3.val=0\xFF\xFF\xFF");

      sendData(TX_TASK_TAG, "Monitor_Dev4.power_dev4.txt=\"0.00W\"\xFF\xFF\xFF");
      sendData(TX_TASK_TAG, "Monitor_Dev4.amp_dev4.txt=\"0.00A\"\xFF\xFF\xFF");
      gpio_set_level(DEVICE_4, OFF);
    }

    if (device.dev1_state && device.dev2_state && device.dev3_state && device.dev4_state) {
      sendData(TX_TASK_TAG, "Control.t9.txt=\"ON\"\xFF\xFF\xFF");	
      sendData(TX_TASK_TAG, "Control.t9.pco=2016\xFF\xFF\xFF");	
      sendData(TX_TASK_TAG, "Control.bt4.val=1\xFF\xFF\xFF");	
      gpio_set_level(DEVICE_1, ON);
      gpio_set_level(DEVICE_2, ON);
      gpio_set_level(DEVICE_3, ON);
      gpio_set_level(DEVICE_4, ON);
    } else if (! (device.dev1_state) ) {
      sendData(TX_TASK_TAG, "Control.t9.txt=\"OFF\"\xFF\xFF\xFF");
      sendData(TX_TASK_TAG, "Control.t9.pco=63488\xFF\xFF\xFF");	
      sendData(TX_TASK_TAG, "Control.bt4.val=0\xFF\xFF\xFF");
      gpio_set_level(DEVICE_1, OFF);
    }else if (! (device.dev2_state) ) {
      sendData(TX_TASK_TAG, "Control.t9.txt=\"OFF\"\xFF\xFF\xFF");
      sendData(TX_TASK_TAG, "Control.t9.pco=63488\xFF\xFF\xFF");	
      sendData(TX_TASK_TAG, "Control.bt4.val=0\xFF\xFF\xFF");
      gpio_set_level(DEVICE_2, OFF);
    }else if (! (device.dev3_state) ) {
      sendData(TX_TASK_TAG, "Control.t9.txt=\"OFF\"\xFF\xFF\xFF");
      sendData(TX_TASK_TAG, "Control.t9.pco=63488\xFF\xFF\xFF");	
      sendData(TX_TASK_TAG, "Control.bt4.val=0\xFF\xFF\xFF");
      gpio_set_level(DEVICE_3, OFF);
    }else if (! (device.dev4_state) ) {
      sendData(TX_TASK_TAG, "Control.t9.txt=\"OFF\"\xFF\xFF\xFF");
      sendData(TX_TASK_TAG, "Control.t9.pco=63488\xFF\xFF\xFF");	
      sendData(TX_TASK_TAG, "Control.bt4.val=0\xFF\xFF\xFF");
      gpio_set_level(DEVICE_4, OFF);
    }
    vTaskDelay(1000 / portTICK_PERIOD_MS); //Transmit every 10 seconds

  }
}

void rx_task(void* arg)
{
  ESP_LOGD(RX_TASK_TAG,"NEXTION RX TASK");
  uint8_t* data = (uint8_t*) malloc(RX_BUFFER+1);
  char *dstream = malloc(RX_BUFFER+1);
  while (1) {
    ESP_LOGD(RX_TASK_TAG, "[APP] Free memory: %d bytes", esp_get_free_heap_size());
    memset(dstream,0,sizeof(malloc(RX_BUFFER+1)));
    const int rxBytes = uart_read_bytes(nUART, data, RX_BUFFER, 500 / portTICK_RATE_MS);
    if (rxBytes > 0) {
      data[rxBytes] = 0;
      snprintf(dstream, RX_BUFFER+1, "%s", data);
    }
    ESP_LOGD(RX_TASK_TAG, "data recv %s", dstream);

    cJSON *sub;
    sub=cJSON_Parse(dstream);

    cJSON *body =cJSON_GetObjectItem(sub,"body");
    char *value_body;
    //char *value_type_cmd;
    if(body){
        value_body=cJSON_GetObjectItem(sub,"body")->valuestring;
        ESP_LOGD(RX_TASK_TAG, "body is %s",value_body);
    }else{
        value_body="";
    }
    cJSON *command =cJSON_GetObjectItem(sub,"command");
    if(command){
        char *value_type_cmd =cJSON_GetObjectItem(sub,"command")->valuestring;
        ESP_LOGD(RX_TASK_TAG, "command is %s",value_type_cmd);
        ParseCmd(value_type_cmd, value_body);
    }
    //if(ParseCmd(value_type_cmd, value_body)==0)continue;
    // free(value_body);
    // free(value_type_cmd);
    vTaskDelay(100/portTICK_PERIOD_MS);
  }
  free(data);
  free(dstream);
}
