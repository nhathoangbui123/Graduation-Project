#include <stdio.h>
#include <string.h>
#include "driver/gpio.h"
#include <stdbool.h>
#include <stdint.h>
#ifdef __cplusplus
extern "C" {
#endif

#define LED_NUMBER                  5
#define PIXEL_SIZE                  12 // each colour takes 4 bytes
#define SAMPLE_RATE                 (93750)
#define ZERO_BUFFER                 48
#define I2S_NUM                     (0)
#define I2S_DO_IO                   (GPIO_NUM_2)
#define I2S_DI_IO                   (-1)

typedef struct {
  uint8_t green;
  uint8_t red;
  uint8_t blue;
} ws2812_pixel_t;

void ws2812_init();

void ws2812_update(ws2812_pixel_t *pixels);

#ifdef __cplusplus
}
#endif