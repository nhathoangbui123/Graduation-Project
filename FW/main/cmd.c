#include "cmd.h"
#include "NVSDriver.h"
#include "nextion.h"
enum 
{
    UNKNOWN_CMD,
    D1ON,               
    D1OFF,  
    D2ON,               
    D2OFF,
    D3ON,               
    D3OFF,
    D4ON,               
    D4OFF,
    ALL_ON,
    ALL_OFF,
    T1,
    T2,
    T3,
    T4,
    EP,
    WN,
    WP,
    RST,
};

char *CMD_STRINGS[] = {
    "  ",                   //UNKNOWN_CMD
    "D1ON",               
    "D1OFF",  
    "D2ON",               
    "D2OFF",
    "D3ON",               
    "D3OFF",
    "D4ON",               
    "D4OFF",
    "ALL_ON",
    "ALL_OFF",
    "T1",
    "T2",
    "T3",
    "T4",
    "EP",
    "WN",
    "WP",
    "RST",
};

#define __NUMBER_OF_CMD_STRINGS (sizeof(CMD_STRINGS) / sizeof(*CMD_STRINGS))
int GetCmd(char *text)
{
	int i = 0;
	for (i = 0; i < __NUMBER_OF_CMD_STRINGS; i++) {
		if (strcmp(text, CMD_STRINGS[i]) == 0) {
			return i;
		}
	}
	return UNKNOWN_CMD;
}
int ParseCmd(char *text, char* body)
{
  ESP_LOGD("CMD_HMI","body: %s", body);
	int cmd_index = GetCmd(text);
    switch (cmd_index) {
      case D1ON:
        ESP_LOGD("CMD_HMI","D1ON");
        gpio_set_level(DEVICE_1, ON);

        led[1].red=0;
        led[1].blue=0;
        led[1].green=255;

        gpio_set_level(BUZZER, 0);
        vTaskDelay( 100 / portTICK_PERIOD_MS );
        gpio_set_level(BUZZER, 1);

        ws2812_update(led);

        device.dev1_state = 1;

        NVSDriverOpen("nvs");
        NVSDriverWrite("Sdev1", "1");
        NVSDriverClose();
        return 0;
      break;
      case D1OFF:
        ESP_LOGD("CMD_HMI","D1OFF");
        gpio_set_level(DEVICE_1, OFF);

        led[1].red=255;
        led[1].blue=0;
        led[1].green=0;

        gpio_set_level(BUZZER, 0);
        vTaskDelay( 100 / portTICK_PERIOD_MS );
        gpio_set_level(BUZZER, 1);

        ws2812_update(led);

        device.dev1_state = 0;

        NVSDriverOpen("nvs");
        NVSDriverWrite("Sdev1", "0");
        NVSDriverClose();
        return 0;
      break;
      case D2ON:
        ESP_LOGD("CMD_HMI","D2ON");
        gpio_set_level(DEVICE_2, ON);

        led[2].red=0;
        led[2].blue=0;
        led[2].green=255;

        gpio_set_level(BUZZER, 0);
        vTaskDelay( 100 / portTICK_PERIOD_MS );
        gpio_set_level(BUZZER, 1);

        ws2812_update(led);

        device.dev2_state = 1;

        NVSDriverOpen("nvs");
        NVSDriverWrite("Sdev2", "1");
        NVSDriverClose();
        return 0;
      break;
      case D2OFF:
        ESP_LOGD("CMD_HMI","D2OFF");
        gpio_set_level(DEVICE_2, OFF);

        led[2].red=255;
        led[2].blue=0;
        led[2].green=0;

        gpio_set_level(BUZZER, 0);
        vTaskDelay( 100 / portTICK_PERIOD_MS );
        gpio_set_level(BUZZER, 1);

        ws2812_update(led);
        device.dev2_state = 0;

        NVSDriverOpen("nvs");
        NVSDriverWrite("Sdev2", "0");
        NVSDriverClose();
        return 0;
      break;
      case D3ON:
        ESP_LOGD("CMD_HMI","D3ON");
        gpio_set_level(DEVICE_3, ON);

        led[3].red=0;
        led[3].blue=0;
        led[3].green=255;

        gpio_set_level(BUZZER, 0);
        vTaskDelay( 100 / portTICK_PERIOD_MS );
        gpio_set_level(BUZZER, 1);

        ws2812_update(led);

        device.dev3_state = 1;

        NVSDriverOpen("nvs");
        NVSDriverWrite("Sdev3", "1");
        NVSDriverClose();
        return 0;
      break;
      case D3OFF:
        ESP_LOGD("CMD_HMI","D3OFF");
        gpio_set_level(DEVICE_3, OFF);

        led[3].red=255;
        led[3].blue=0;
        led[3].green=0;

        gpio_set_level(BUZZER, 0);
        vTaskDelay( 100 / portTICK_PERIOD_MS );
        gpio_set_level(BUZZER, 1);

        ws2812_update(led);

        device.dev3_state = 0;

        NVSDriverOpen("nvs");
        NVSDriverWrite("Sdev3", "0");
        NVSDriverClose();
        return 0;
      break;
      case D4ON:
        ESP_LOGD("CMD_HMI","D4ON");
        gpio_set_level(DEVICE_4, ON);

        led[4].red=0;
        led[4].blue=0;
        led[4].green=255;
        
        gpio_set_level(BUZZER, 0);
        vTaskDelay( 100 / portTICK_PERIOD_MS );
        gpio_set_level(BUZZER, 1);

        ws2812_update(led);

        device.dev4_state = 1;

        NVSDriverOpen("nvs");
        NVSDriverWrite("Sdev4", "1");
        NVSDriverClose();
        return 0;
      break;
      case D4OFF:
        ESP_LOGD("CMD_HMI","D4OFF");
        gpio_set_level(DEVICE_4, OFF);

        led[4].red=255;
        led[4].blue=0;
        led[4].green=0;

        gpio_set_level(BUZZER, 0);
        vTaskDelay( 100 / portTICK_PERIOD_MS );
        gpio_set_level(BUZZER, 1);

        ws2812_update(led);

        device.dev4_state = 0;

        NVSDriverOpen("nvs");
        NVSDriverWrite("Sdev4", "0");
        NVSDriverClose();
        return 0;
      break;
      case ALL_ON:
        ESP_LOGD("CMD_HMI","ALL_ON");
        gpio_set_level(DEVICE_1, ON);
        gpio_set_level(DEVICE_2, ON);
        gpio_set_level(DEVICE_3, ON);
        gpio_set_level(DEVICE_4, ON);

        led[1].red=0;
        led[1].blue=0;
        led[1].green=255;

        led[2].red=0;
        led[2].blue=0;
        led[2].green=255;

        led[3].red=0;
        led[3].blue=0;
        led[3].green=255;

        led[4].red=0;
        led[4].blue=0;
        led[4].green=255;

        gpio_set_level(BUZZER, 0);
        vTaskDelay( 100 / portTICK_PERIOD_MS );
        gpio_set_level(BUZZER, 1);

        ws2812_update(led);

        device.dev1_state = 1;
        device.dev2_state = 1;
        device.dev3_state = 1;
        device.dev4_state = 1;

        NVSDriverOpen("nvs");
        NVSDriverWrite("Sdev1", "1");
        NVSDriverWrite("Sdev2", "1");
        NVSDriverWrite("Sdev3", "1");
        NVSDriverWrite("Sdev4", "1");
        NVSDriverClose();
        return 0;
      break;
      case ALL_OFF:
        ESP_LOGD("CMD_HMI","ALL_OFF");
        gpio_set_level(DEVICE_1, OFF);
        gpio_set_level(DEVICE_2, OFF);
        gpio_set_level(DEVICE_3, OFF);
        gpio_set_level(DEVICE_4, OFF);

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

        gpio_set_level(BUZZER, 0);
        vTaskDelay( 100 / portTICK_PERIOD_MS );
        gpio_set_level(BUZZER, 1);

        ws2812_update(led);

        device.dev1_state = 0;
        device.dev2_state = 0;
        device.dev3_state = 0;
        device.dev4_state = 0;
        
        NVSDriverOpen("nvs");
        NVSDriverWrite("Sdev1", "0");
        NVSDriverWrite("Sdev2", "0");
        NVSDriverWrite("Sdev3", "0");
        NVSDriverWrite("Sdev4", "0");
        NVSDriverClose();
        return 0;
      break;
      case T1:
        ESP_LOGD("CMD_HMI","T1");

        gpio_set_level(BUZZER, 0);
        vTaskDelay( 100 / portTICK_PERIOD_MS );
        gpio_set_level(BUZZER, 1);

        NVSDriverOpen("nvs");
        NVSDriverWrite("T1", body);
        NVSDriverClose();

        param.T1=atoi(body);
      break;
      case T2:
        ESP_LOGD("CMD_HMI","T2");

        gpio_set_level(BUZZER, 0);
        vTaskDelay( 100 / portTICK_PERIOD_MS );
        gpio_set_level(BUZZER, 1);

        NVSDriverOpen("nvs");
        NVSDriverWrite("T2", body);
        NVSDriverClose();

        param.T2=atoi(body);
      break;
      case T3:
        ESP_LOGD("CMD_HMI","T3");

        gpio_set_level(BUZZER, 0);
        vTaskDelay( 100 / portTICK_PERIOD_MS );
        gpio_set_level(BUZZER, 1);

        NVSDriverOpen("nvs");
        NVSDriverWrite("T3", body);
        NVSDriverClose();

        param.T3=atoi(body);
      break;
      case T4:
        ESP_LOGD("CMD_HMI","T4");

        gpio_set_level(BUZZER, 0);
        vTaskDelay( 100 / portTICK_PERIOD_MS );
        gpio_set_level(BUZZER, 1);

        NVSDriverOpen("nvs");
        NVSDriverWrite("T4", body);
        NVSDriverClose();

        param.T4=atoi(body);
      break;
      case EP:
        ESP_LOGD("CMD_HMI","EP");

        gpio_set_level(BUZZER, 0);
        vTaskDelay( 100 / portTICK_PERIOD_MS );
        gpio_set_level(BUZZER, 1);

        NVSDriverOpen("nvs");
        NVSDriverWrite("EP", body);
        NVSDriverClose();

        param.EP=atoi(body);
      break;
      case WN:
        ESP_LOGD("CMD_HMI","WN");

        gpio_set_level(BUZZER, 0);
        vTaskDelay( 100 / portTICK_PERIOD_MS );
        gpio_set_level(BUZZER, 1);

        NVSDriverOpen("nvs");
        NVSDriverWrite("WN", body);
        NVSDriverClose();

        param.WN=strdup(body);
      break;
      case WP:
        ESP_LOGD("CMD_HMI","WP");
        
        gpio_set_level(BUZZER, 0);
        vTaskDelay( 100 / portTICK_PERIOD_MS );
        gpio_set_level(BUZZER, 1);

        NVSDriverOpen("nvs");
        NVSDriverWrite("WP", body);
        NVSDriverClose();

        param.WP=strdup(body);
      break;
      case RST:
        ESP_LOGD("CMD_HMI","RST");
        ESP_LOGD("CMD_HMI","ESP32 Will restart");
        char *cmd = (char*)malloc(256);
        for(int i=0;i<=100;i++){
          sprintf(cmd, "ProgressReset.j0.val=%d\xFF\xFF\xFF",i);
          //ESP_LOGD(TX_TASK_TAG, "Monitor_Dev1.thr_dev1.txt=%dkWh", param.T1);
          sendData("CMD_HMI", cmd);
          vTaskDelay(10/ portTICK_PERIOD_MS);
        }
        esp_restart();
      break;
      case UNKNOWN_CMD:
        ESP_LOGD("CMD_HMI","UART RX: cmd UNKNOWN_CMD");
      return 1;
      break;
	}
	return 0;
}