#include <stdio.h>
#include <stdint.h>
#include <stddef.h>
#include <string.h>
#include <math.h>

#include "esp_system.h"
#include "esp_event.h"
#include "esp_netif.h"
#include "esp_log.h"
#include "nvs_flash.h"
#include "driver/uart.h"
#include "driver/gpio.h"

#include "config.h"
#include "aws.h"
#include "pzem.h"
#include "nextion.h"
#include "NVSDriver.h"
#include "wifi.h"

static const char *TAG = "MAIN";

static const int RX_BUF_SIZE = 1024;
void thersh_task(void *arg){
    while (1) {
        if(param.P1>param.T1||param.P2>param.T2||param.P3>param.T3||param.P4>param.T4){
            gpio_set_level(BUZZER, 0);
            sendData(TX_TASK_TAG, "page 23\xFF\xFF\xFF");
        }else{
            gpio_set_level(BUZZER, 1);
            //sendData(TX_TASK_TAG, "page 1\xFF\xFF\xFF");
        }

        if(param.P1>param.T1){
            param.T1F=1;
            for(int i=0;i<5;i++){
                led[1].red=255;
                led[1].blue=0;
                led[1].green=255;
                ws2812_update(led);
                vTaskDelay( 200/ portTICK_PERIOD_MS );

                led[1].red=0;
                led[1].blue=0;
                led[1].green=0;
                ws2812_update(led);
                vTaskDelay( 200/ portTICK_PERIOD_MS );
            }
        }else{
            param.T1F=0;
            if(device.dev1_state){
                led[1].red=0;
                led[1].blue=0;
                led[1].green=255;
                ws2812_update(led);
            }else{
                led[1].red=255;
                led[1].blue=0;
                led[1].green=0;
                ws2812_update(led);
            }
        }
        if(param.P2>param.T2){
            param.T2F=1;
            for(int i=0;i<5;i++){
                led[2].red=255;
                led[2].blue=0;
                led[2].green=255;
                ws2812_update(led);
                vTaskDelay( 200/ portTICK_PERIOD_MS );

                led[2].red=0;
                led[2].blue=0;
                led[2].green=0;
                ws2812_update(led);
                vTaskDelay( 200/ portTICK_PERIOD_MS );
            }
        }else{
            param.T2F=0;
            if(device.dev2_state){
                led[2].red=0;
                led[2].blue=0;
                led[2].green=255;
                ws2812_update(led);
            }else{
                led[2].red=255;
                led[2].blue=0;
                led[2].green=0;
                ws2812_update(led);
            }
        }
        if(param.P3>param.T3){
            param.T3F=1;
            for(int i=0;i<5;i++){
                led[3].red=255;
                led[3].blue=0;
                led[3].green=255;
                ws2812_update(led);
                vTaskDelay( 200/ portTICK_PERIOD_MS );

                led[3].red=0;
                led[3].blue=0;
                led[3].green=0;
                ws2812_update(led);
                vTaskDelay( 200/ portTICK_PERIOD_MS );
            }
        }else{
            param.T3F=0;
            if(device.dev3_state){
                led[3].red=0;
                led[3].blue=0;
                led[3].green=255;
                ws2812_update(led);
            }else{
                led[3].red=255;
                led[3].blue=0;
                led[3].green=0;
                ws2812_update(led);
            }
        }
        if(param.P4>param.T4){
            param.T4F=1;
            for(int i=0;i<5;i++){
                led[4].red=255;
                led[4].blue=0;
                led[4].green=255;
                ws2812_update(led);
                vTaskDelay( 200/ portTICK_PERIOD_MS );

                led[4].red=0;
                led[4].blue=0;
                led[4].green=0;
                ws2812_update(led);
                vTaskDelay( 200/ portTICK_PERIOD_MS );
            }
        }else{
            param.T4F=0;
            if(device.dev4_state){
                led[4].red=0;
                led[4].blue=0;
                led[4].green=255;
                ws2812_update(led);
            }else{
                led[4].red=255;
                led[4].blue=0;
                led[4].green=0;
                ws2812_update(led);
            }
        }
        vTaskDelay( 50/ portTICK_PERIOD_MS );
    }
}
void pzem_task(void *arg){
    uart_data_t uart_data= {
        .uart_port = UART_NUM_1,
        .tx_io_num = TX_PZEM,
        .rx_io_num = RX_PZEM,
    };
    uart_config_t uart_config = {
	        .baud_rate = PZEM_BAUD_RATE,
	        .data_bits = UART_DATA_8_BITS,
	        .parity = UART_PARITY_DISABLE,
	        .stop_bits = UART_STOP_BITS_1,
	        .flow_ctrl = UART_HW_FLOWCTRL_DISABLE,    //UART_HW_FLOWCTRL_CTS_RTS,
	        .rx_flow_ctrl_thresh = 122,
	};

	ESP_ERROR_CHECK(uart_driver_install(uart_data.uart_port, RX_BUF_SIZE * 2, 0, 0, NULL, 0));
	ESP_ERROR_CHECK(uart_param_config(uart_data.uart_port, &uart_config));
	ESP_ERROR_CHECK(uart_set_pin(uart_data.uart_port, uart_data.tx_io_num, uart_data.rx_io_num, UART_PIN_NO_CHANGE, UART_PIN_NO_CHANGE));

    pzem pzemobj[4];
    pzemobj[0]=pzem(0x42);
    
    pzemobj[1]=pzem(0x43);

    pzemobj[2]=pzem(0x44);

    pzemobj[3]=pzem(0x45);

    while (1) {
        ESP_LOGI("TAG_PZEM004T", "[APP] Free memory: %d bytes", esp_get_free_heap_size());
        // ESP_LOGI("TAG_PZEM004T", "Address pzem[0] %#.2x", pzemobj[0].getAddress());
        // ESP_LOGI("TAG_PZEM004T", "Address pzem[1] %#.2x", pzemobj[1].getAddress());
        // ESP_LOGI("TAG_PZEM004T", "Address pzem[2] %#.2x", pzemobj[2].getAddress());
        // ESP_LOGI("TAG_PZEM004T", "Address pzem[3] %#.2x", pzemobj[3].getAddress());

        param.U1=pzemobj[0].voltage();
        param.I1=pzemobj[0].current();
        param.P1=pzemobj[0].power();
        param.E1=pzemobj[0].energy();
        param.F1=pzemobj[0].frequency();
        vTaskDelay( 1000 / portTICK_PERIOD_MS );

        param.U2=pzemobj[1].voltage();
        param.I2=pzemobj[1].current();
        param.P2=pzemobj[1].power();
        param.E2=pzemobj[1].energy();
        param.F2=pzemobj[1].frequency();
        vTaskDelay( 1000 / portTICK_PERIOD_MS );

        param.U3=pzemobj[2].voltage();
        param.I3=pzemobj[2].current();
        param.P3=pzemobj[2].power();
        param.E3=pzemobj[2].energy();
        param.F3=pzemobj[2].frequency();
        vTaskDelay( 1000 / portTICK_PERIOD_MS );

        param.U4=pzemobj[3].voltage();
        param.I4=pzemobj[3].current();
        param.P4=pzemobj[3].power();
        param.E4=pzemobj[3].energy();
        param.F4=pzemobj[3].frequency();
        vTaskDelay( 1000 / portTICK_PERIOD_MS );
        
        //total
        param.current = param.I1+param.I2+param.I3+param.I4;
        param.voltage = (param.U1+param.U2+param.U3+param.U4)/4;
        param.frequency = (param.F1+param.F2+param.F3+param.F4)/4;
        param.energy = (param.E1+param.E2+param.E3+param.E4);
        param.cost = param.energy*param.EP;
        //power device
        // param.P1=param.voltage*param.I1*(1/sqrt(2));
        // param.P2=param.voltage*param.I2*(1/sqrt(2));
        // param.P3=param.voltage*param.I3*(1/sqrt(2));
        // param.P4=param.voltage*param.I4*(1/sqrt(2));
        //user1
        param.current1=param.I1+param.I2;
        param.energy1=param.E1+param.E2;
        param.cost1=param.energy1*param.EP;
        //user2
        param.current2=param.I3+param.I4;
        param.energy2=param.E3+param.E4;
        param.cost2=param.energy2*param.EP;
        //cost device
        param.C1=param.E1*param.EP;
        param.C2=param.E2*param.EP;
        param.C3=param.E3*param.EP;
        param.C4=param.E4*param.EP;

        ESP_LOGI("TAG_PZEM004T","voltage   dev1=%f",param.U1);
        ESP_LOGI("TAG_PZEM004T","current   dev1=%f",param.I1);
        ESP_LOGI("TAG_PZEM004T","power     dev1=%f",param.P1);
        ESP_LOGI("TAG_PZEM004T","energy    dev1=%f",param.E1);
        ESP_LOGI("TAG_PZEM004T","frequency dev1=%f",param.F1);

        ESP_LOGI("TAG_PZEM004T","voltage   dev2=%f",param.U2);
        ESP_LOGI("TAG_PZEM004T","current   dev2=%f",param.I2);
        ESP_LOGI("TAG_PZEM004T","power     dev2=%f",param.P2);
        ESP_LOGI("TAG_PZEM004T","energy    dev2=%f",param.E2);
        ESP_LOGI("TAG_PZEM004T","frequency dev2=%f",param.F2);

        ESP_LOGI("TAG_PZEM004T","voltage   dev3=%f",param.U3);
        ESP_LOGI("TAG_PZEM004T","current   dev3=%f",param.I3);
        ESP_LOGI("TAG_PZEM004T","power     dev3=%f",param.P3);
        ESP_LOGI("TAG_PZEM004T","energy    dev3=%f",param.E3);
        ESP_LOGI("TAG_PZEM004T","frequency dev3=%f",param.F3);

        ESP_LOGI("TAG_PZEM004T","voltage   dev4=%f",param.U4);
        ESP_LOGI("TAG_PZEM004T","current   dev4=%f",param.I4);
        ESP_LOGI("TAG_PZEM004T","power     dev4=%f",param.P4);
        ESP_LOGI("TAG_PZEM004T","energy    dev4=%f",param.E4);
        ESP_LOGI("TAG_PZEM004T","frequency dev4=%f",param.F4);

        vTaskDelay( 6000 / portTICK_PERIOD_MS );
	}
}
extern "C"  void app_main(){
    ESP_LOGI(TAG, "[APP] Startup..");
    ESP_LOGI(TAG, "[APP] Free memory: %d bytes", esp_get_free_heap_size());
    ESP_LOGI(TAG, "[APP] IDF version: %s", esp_get_idf_version());

    /* Initialize NVS partition */
    esp_err_t ret = nvs_flash_init();
    if (ret == ESP_ERR_NVS_NO_FREE_PAGES || ret == ESP_ERR_NVS_NEW_VERSION_FOUND) {
        ESP_ERROR_CHECK(nvs_flash_init());
    }
    
    NVSDriverOpen("nvs");

    char*Sdev1=NULL;
    NVSDriverRead("Sdev1", &Sdev1);
    if(Sdev1!=NULL){
        device.dev1_state=atoi(Sdev1);
    }
    free(Sdev1);

    char*Sdev2=NULL;
    NVSDriverRead("Sdev2", &Sdev2);
    if(Sdev2!=NULL){
        device.dev2_state=atoi(Sdev2);
    }
    free(Sdev2);

    char*Sdev3=NULL;
    NVSDriverRead("Sdev3", &Sdev3);
    if(Sdev3!=NULL){
        device.dev3_state=atoi(Sdev3);
    }
    free(Sdev3);

    char*Sdev4=NULL;
    NVSDriverRead("Sdev4", &Sdev4);
    if(Sdev4!=NULL){
        device.dev4_state=atoi(Sdev4);
    }
    free(Sdev4);

    char*T1=NULL;
    NVSDriverRead("T1", &T1);
    if(T1!=NULL){
        param.T1=atoi(T1);
    }
    free(T1);

    char*T2=NULL;
    NVSDriverRead("T2", &T2);
    if(T2!=NULL){
        param.T2=atoi(T2);
    }
    free(T2);

    char*T3=NULL;
    NVSDriverRead("T3", &T3);
    if(T3!=NULL){
        param.T3=atoi(T3);
    }
    free(T3);

    char*T4=NULL;
    NVSDriverRead("T4", &T4);
    if(T4!=NULL){
        param.T4=atoi(T4);
    }
    free(T4);

    char*EP=NULL;
    NVSDriverRead("EP", &EP);
    if(EP!=NULL){
        param.EP=atoi(EP);
    }
    free(EP);
    
    if(NVSDriverRead("WN", &param.WN)!=true){
        param.WN="1";
    }
    if(NVSDriverRead("WP", &param.WP)!=true){
        param.WP="1";
    }

    NVSDriverClose();

    //device
    gpio_pad_select_gpio(DEVICE_1);
    gpio_pad_select_gpio(DEVICE_2);
    gpio_pad_select_gpio(DEVICE_3);
    gpio_pad_select_gpio(DEVICE_4);
    gpio_set_direction(DEVICE_1, GPIO_MODE_OUTPUT);
    gpio_set_direction(DEVICE_2, GPIO_MODE_OUTPUT);
    gpio_set_direction(DEVICE_3, GPIO_MODE_OUTPUT);
    gpio_set_direction(DEVICE_4, GPIO_MODE_OUTPUT);
    gpio_set_level(DEVICE_1, 1);
    gpio_set_level(DEVICE_2, 1);
    gpio_set_level(DEVICE_3, 1);
    gpio_set_level(DEVICE_4, 1);
    //buzzer
    gpio_pad_select_gpio(BUZZER);
    gpio_set_direction(BUZZER, GPIO_MODE_OUTPUT);
    gpio_set_level(BUZZER, 1);
    //led
    ws2812_init();

    led[0].red=255;
    led[0].blue=0;
    led[0].green=0;

    led[1].red=255;
    led[1].blue=0;
    led[1].green=0;

    led[2].red=255;
    led[2].blue=0;
    led[2].green=0;

    led[3].red=255;
    led[3].blue=0;
    led[3].green=0;

    led[4].red=255;
    led[4].blue=0;
    led[4].green=0;
    ws2812_update(led);

    //nextion hmi
    initNextion();
    //pzem task
    xTaskCreate(&pzem_task, "pzem_task", 9216, NULL, 9, NULL);
    //rx task
    xTaskCreate(&rx_task, "uart_rx_task", 1024*2, NULL, 1, NULL);
    //tx task
    xTaskCreate(&tx_task, "uart_tx_task", 1024*2, NULL, 2, NULL);
    
    //wifi
    if(wifi_init_sta()){
        //beep
        gpio_set_level(BUZZER, 0);
        vTaskDelay( 100 / portTICK_PERIOD_MS );
        gpio_set_level(BUZZER, 1);
        //led orange
        led[0].red=255;
        led[0].blue=0;
        led[0].green=255;
        ws2812_update(led);

        //Set wifi icon on the screen
        sendData(TX_TASK_TAG, "page 0\xFF\xFF\xFF");
        //Set led to green
        led[0].red=0;
        led[0].blue=0;
        led[0].green=255;
        ws2812_update(led);

        xTaskCreate(&aws_iot_task, "aws_iot_task", 12*1024,NULL, 10, NULL);
    }else{
        for(int i=0;i<5;i++){
            gpio_set_level(BUZZER, 0);
            vTaskDelay( 100 / portTICK_PERIOD_MS );
            gpio_set_level(BUZZER, 1);

            vTaskDelay( 500/ portTICK_PERIOD_MS );
        }
        //Set led to orange
        led[0].red=255;
        led[0].blue=0;
        led[0].green=255;
        ws2812_update(led);
    }

    vTaskDelay(1000/ portTICK_PERIOD_MS);

    sendData(TX_TASK_TAG, "page 1\xFF\xFF\xFF");
    vTaskDelay(5 / portTICK_PERIOD_MS);
    sendData(TX_TASK_TAG, "Monitor.wifi.pic=10\xFF\xFF\xFF");
    sendData(TX_TASK_TAG, "ProgressReset.j0.val=0\xFF\xFF\xFF");
    vTaskDelay(5 / portTICK_PERIOD_MS);
    
    //thresh hold task
    xTaskCreate(&thersh_task, "thersh_task", 1024*2, NULL, 3, NULL);
    ESP_LOGI(TAG, "[APP] Free memory: %d bytes", esp_get_free_heap_size());
}