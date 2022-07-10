#ifndef __ADC__
#define __ADC__
#include "driver/adc.h"
#include "esp_adc_cal.h"
#include "esp_log.h"

#include "freertos/FreeRTOS.h"
#include "freertos/task.h"

//ADC Channels
#define ADC1_DEV1          ADC1_CHANNEL_6 //gpio 34
#define ADC1_DEV2          ADC1_CHANNEL_7 //gpio 35
#define ADC1_DEV3          ADC1_CHANNEL_4 //gpio 32
#define ADC1_DEV4          ADC1_CHANNEL_5 //gpio 33
//ADC Attenuation
#define ADC_ATTEN           ADC_ATTEN_DB_0
//ADC Calibration
#define ADC_CALI_SCHEME     ESP_ADC_CAL_VAL_EFUSE_VREF
bool adc_calibration_init(void);
void adc_task(void *arg);
#endif //__ADC__