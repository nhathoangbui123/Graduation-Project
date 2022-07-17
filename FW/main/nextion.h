#ifndef __nextion__
#define __nextion__

#include "freertos/FreeRTOS.h"
#include "freertos/task.h"
#include "esp_system.h"
#include "esp_log.h"
#include "driver/uart.h"
#include "driver/gpio.h"
#include "soc/uart_struct.h"
#include "string.h"
#include <stdio.h>
#include "cmd.h"

#ifdef __cplusplus
extern "C" {
#endif

static const int RX_BUFFER = 1024;
static const int TX_BUFFER = 256;

static const char *TX_TASK_TAG = "TX_TASK";
static const char *RX_TASK_TAG = "RX_TASK";

// void nextion_main(param_t param);
int sendData(const char* logName, const char* data);
void initNextion();
void tx_task(void* arg);
void rx_task(void* arg);

#ifdef __cplusplus
}
#endif
#endif //__nextion__

