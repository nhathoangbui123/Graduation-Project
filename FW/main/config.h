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

#ifdef __cplusplus
extern "C" {
#endif

typedef struct {
    float voltage;
    float current;
    float power;
    float energy;
    float frequency;
    float pf;
    uint16_t alarms;
    float cost;

    float U1;
    float I1;
    float E1;
    float P1;
    float F1;
    float C1;
    int T1;


    float U2;
    float I2;
    float E2;
    float P2;
    float F2;
    float C2;
    int T2;

    float U3;
    float I3;
    float E3;
    float P3;
    float F3;
    float C3;
    int T3;

    float U4;
    float I4;
    float E4;
    float P4;
    float F4;
    float C4;
    int T4;

    int EP;
    char* WN;
    char* WP;
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

#ifdef __cplusplus
}
#endif

#endif