#include "adc_dev.h"
#include <stdio.h>
#include <math.h>
#include "config.h"
static int adc_raw[4];
static esp_adc_cal_characteristics_t *adc1_chars;
#define ADC_COUNTS  (1<<12)

static void check_efuse(void)
{
    //Check if TP is burned into eFuse
    if (esp_adc_cal_check_efuse(ESP_ADC_CAL_VAL_EFUSE_TP) == ESP_OK) {
        printf("eFuse Two Point: Supported\n");
    } else {
        printf("eFuse Two Point: NOT supported\n");
    }
    //Check Vref is burned into eFuse
    if (esp_adc_cal_check_efuse(ESP_ADC_CAL_VAL_EFUSE_VREF) == ESP_OK) {
        printf("eFuse Vref: Supported\n");
    } else {
        printf("eFuse Vref: NOT supported\n");
    }
}
static void print_char_val_type(esp_adc_cal_value_t val_type)
{
    if (val_type == ESP_ADC_CAL_VAL_EFUSE_TP) {
        printf("Characterized using Two Point Value\n");
    } else if (val_type == ESP_ADC_CAL_VAL_EFUSE_VREF) {
        printf("Characterized using eFuse Vref\n");
    } else {
        printf("Characterized using Default Vref\n");
    }
}
void adc_task(void *arg)
{
    //Check if Two Point or Vref are burned into eFuse
    check_efuse();
    //ADC1 config
    ESP_ERROR_CHECK(adc1_config_width(ADC_WIDTH_BIT_12));
    ESP_ERROR_CHECK(adc1_config_channel_atten(ADC1_DEV1, ADC_ATTEN));
    ESP_ERROR_CHECK(adc1_config_channel_atten(ADC1_DEV2, ADC_ATTEN));
    ESP_ERROR_CHECK(adc1_config_channel_atten(ADC1_DEV3, ADC_ATTEN));
    ESP_ERROR_CHECK(adc1_config_channel_atten(ADC1_DEV4, ADC_ATTEN));

    //Characterize ADC
    adc1_chars = calloc(1, sizeof(esp_adc_cal_characteristics_t));
    esp_adc_cal_value_t val_type = esp_adc_cal_characterize(ADC_UNIT_1, ADC_ATTEN, ADC_WIDTH_BIT_12, 1100, adc1_chars);
    print_char_val_type(val_type);

    double offsetI=0;
    offsetI = ADC_COUNTS>>1;
    double filteredI=0;
    double sqI=0,sumI=0;
    double ICAL=1.2625;
    while (1) {
        //dev1
        for (unsigned int n = 0; n < 4096; n++)
        {
            adc_raw[0]= adc1_get_raw(ADC1_DEV1);
            //sampleI = analogRead(inPinI);

            // Digital low pass filter extracts the 2.5 V or 1.65 V dc offset,
            //  then subtract this - signal is now centered on 0 counts.
            offsetI = (offsetI + (adc_raw[0]-offsetI)/1024);
            filteredI = adc_raw[0] - offsetI;

            // Root-mean-square method current
            // 1) square current values
            sqI = filteredI * filteredI;
            // 2) sum
            sumI += sqI;
        }
        double I_RATIO = ICAL *((3300.0/1000.0) / (ADC_COUNTS));
        param.I1 = ((I_RATIO * sqrt(sumI / 4096)));
        param.P1 = param.I1 * param.voltage;
        //Reset accumulators
        sumI = 0;
        ESP_LOGI("ADC","param.I1=%0.3f A", param.I1);
        ESP_LOGI("ADC","param.P1=%0.3f W", param.P1);

        // //dev2
        // for (unsigned int n = 0; n < 4096; n++)
        // {
        //     adc_raw[1]= adc1_get_raw(ADC1_DEV2);
        //     //sampleI = analogRead(inPinI);

        //     // Digital low pass filter extracts the 2.5 V or 1.65 V dc offset,
        //     //  then subtract this - signal is now centered on 0 counts.
        //     offsetI = (offsetI + (adc_raw[1]-offsetI)/1024);
        //     filteredI = adc_raw[1] - offsetI;

        //     // Root-mean-square method current
        //     // 1) square current values
        //     sqI = filteredI * filteredI;
        //     // 2) sum
        //     sumI += sqI;
        // }
        // I_RATIO = ICAL *((3300.0/1000.0) / (ADC_COUNTS));
        // param.I2 = I_RATIO * sqrt(sumI / 4096);
        // param.P2 = param.I2 * param.voltage;
        // //Reset accumulators
        // sumI = 0;
        // // ESP_LOGI("ADC","param.I2=%0.3f A", param.I2);
        // // ESP_LOGI("ADC","param.P2=%0.3f W", param.P2);

        // //dev3
        // for (unsigned int n = 0; n < 4096; n++)
        // {
        //     adc_raw[2]= adc1_get_raw(ADC1_DEV3);
        //     //sampleI = analogRead(inPinI);

        //     // Digital low pass filter extracts the 2.5 V or 1.65 V dc offset,
        //     //  then subtract this - signal is now centered on 0 counts.
        //     offsetI = (offsetI + (adc_raw[2]-offsetI)/1024);
        //     filteredI = adc_raw[2] - offsetI;

        //     // Root-mean-square method current
        //     // 1) square current values
        //     sqI = filteredI * filteredI;
        //     // 2) sum
        //     sumI += sqI;
        // }
        // I_RATIO = ICAL *((3300.0/1000.0) / (ADC_COUNTS));
        // param.I3 = I_RATIO * sqrt(sumI / 4096);
        // param.P3 = param.I3 * param.voltage;
        // //Reset accumulators
        // sumI = 0;
        // // ESP_LOGI("ADC","param.I3=%0.3f A", param.I3);
        // // ESP_LOGI("ADC","param.P3=%0.3f W", param.P3);

        // //dev4
        // for (unsigned int n = 0; n < 4096; n++)
        // {
        //     adc_raw[3]= adc1_get_raw(ADC1_DEV4);
        //     //sampleI = analogRead(inPinI);

        //     // Digital low pass filter extracts the 2.5 V or 1.65 V dc offset,
        //     //  then subtract this - signal is now centered on 0 counts.
        //     offsetI = (offsetI + (adc_raw[3]-offsetI)/1024);
        //     filteredI = adc_raw[3] - offsetI;

        //     // Root-mean-square method current
        //     // 1) square current values
        //     sqI = filteredI * filteredI;
        //     // 2) sum
        //     sumI += sqI;
        // }
        // I_RATIO = ICAL *((3300.0/1000.0) / (ADC_COUNTS));
        // param.I4 = I_RATIO * sqrt(sumI / 4096);
        // param.P4 = param.I4 * param.voltage;
        // //Reset accumulators
        // sumI = 0;
        // ESP_LOGI("ADC","param.I4=%0.3f A", param.I4);
        // ESP_LOGI("ADC","param.P4=%0.3f W", param.P4);
        vTaskDelay(100/ portTICK_PERIOD_MS );
    }
}