#ifndef __CMD__
#define __CMD__
#include "config.h"
#include "freertos/FreeRTOS.h"
#include "freertos/task.h"
int ParseCmd(char *text, char* body);
int GetCmd(char *text);
#endif