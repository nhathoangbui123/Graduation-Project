#ifndef __CONFIG__
#define __CONFIG__
#include "stdint.h"
#include "stdbool.h"
#include "esp_log.h"
#include "ws2812_driver.h"

#define Client_ID "ESP32"
#define Topic_Pub Client_ID"/pub"
#define Topic_Sub Client_ID"/sub"

#define BUZZER      GPIO_NUM_4

#define TX_PZEM     GPIO_NUM_18
#define RX_PZEM     GPIO_NUM_19

#define DEVICE_1    GPIO_NUM_26
#define DEVICE_2    GPIO_NUM_27
#define DEVICE_3    GPIO_NUM_14
#define DEVICE_4    GPIO_NUM_13

#define TX_HMI      GPIO_NUM_22
#define RX_HMI      GPIO_NUM_23

#define nUART	   (UART_NUM_2)
#define OFF           1
#define ON            0

typedef struct {
    float voltage;
    float current;
    float power;
    float energy;
    float frequency;
    float pf;
    uint16_t alarms;

    float I1;
    float P1;
    int T1;


    float I2;
    float P2;
    int T2;

    float I3;
    float P3;
    int T3;

    float I4;
    float P4;
    int T4;

    int EP;
    char* WN;
    char* WP;

    float cost;
} param_t; // Measured values

param_t param;

typedef struct
{
    int dev1_state;
    int dev2_state;
    int dev3_state;
    int dev4_state;
}devstate_t;

devstate_t device;

ws2812_pixel_t led[5];
#endif