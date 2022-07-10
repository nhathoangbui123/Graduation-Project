#include <stdio.h>
#include <stdint.h>
#include <stddef.h>
#include <string.h>

#include "esp_system.h"
#include "esp_event.h"
#include "esp_netif.h"
#include "esp_log.h"
#include "protocol_examples_common.h"
#include "nvs_flash.h"
#include "driver/uart.h"
#include "driver/gpio.h"

#include "config.h"
#include "aws.h"
#include "pzem.h"
#include "adc_dev.h"
#include "nextion.h"
#include "NVSDriver.h"
#include "wifi.h"
static const char *TAG = "MAIN";

void app_main(){
    ESP_LOGI(TAG, "[APP] Startup..");
    ESP_LOGI(TAG, "[APP] Free memory: %d bytes", esp_get_free_heap_size());
    ESP_LOGI(TAG, "[APP] IDF version: %s", esp_get_idf_version());

    /* Initialize NVS partition */
    esp_err_t ret = nvs_flash_init();
    if (ret == ESP_ERR_NVS_NO_FREE_PAGES || ret == ESP_ERR_NVS_NEW_VERSION_FOUND) {
        /* NVS partition was truncated
        * and needs to be erased */
        //ESP_ERROR_CHECK(nvs_flash_erase());

        /* Retry nvs_flash_init */
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

    NVSDriverRead("WN", &param.WN);
    NVSDriverRead("WP", &param.WP);

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
    //ws2812_pixel_t led[5];
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
    
    // ESP_ERROR_CHECK(esp_netif_init());
    // ESP_ERROR_CHECK(esp_event_loop_create_default());
    // ESP_ERROR_CHECK(example_connect());
    wifi_init_sta();
    
    gpio_set_level(BUZZER, 0);
    vTaskDelay( 100 / portTICK_PERIOD_MS );
    gpio_set_level(BUZZER, 1);

    led[0].red=255;
    led[0].blue=0;
    led[0].green=255;
    ws2812_update(led);

    initNextion();
    //Set wifi icon on the screen
    sendData(TX_TASK_TAG, "page 0\xFF\xFF\xFF");
    vTaskDelay(2000/ portTICK_PERIOD_MS);

    // param.I1=1;
    // param.I2=2;
    // param.I3=3;
    // param.I4=4;

    xTaskCreate(&aws_iot_task, "aws_iot_task", 10*1024,NULL, 10, NULL);
    xTaskCreate(&pzem_task, "pzem_task", 9216, NULL, 9, NULL);
    xTaskCreate(&adc_task, "adc_task", 1024*2, NULL, 8, NULL);
    xTaskCreate(&rx_task, "uart_rx_task", 1024*2, NULL, 1, NULL);
    xTaskCreate(&tx_task, "uart_tx_task", 1024*2, NULL, 2, NULL);
    
    led[0].red=0;
    led[0].blue=0;
    led[0].green=255;
    ws2812_update(led);

    sendData(TX_TASK_TAG, "page 1\xFF\xFF\xFF");
    vTaskDelay(5 / portTICK_PERIOD_MS);
    sendData(TX_TASK_TAG, "Monitor.wifi.pic=10\xFF\xFF\xFF");
    sendData(TX_TASK_TAG, "ProgressReset.j0.val=0\xFF\xFF\xFF");
    vTaskDelay(5 / portTICK_PERIOD_MS);
}