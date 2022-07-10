#ifndef __AWS__
#define __AWS__

#include "aws_iot_config.h"
#include "aws_iot_log.h"
#include "aws_iot_version.h"
#include "aws_iot_mqtt_client_interface.h"
#include "string.h"
#include "cJSON.h"
static void iot_subscribe_callback_handler(AWS_IoT_Client *pClient, char *topicName, uint16_t topicNameLen,
                                            IoT_Publish_Message_Params *params, void *pData);
static void disconnectCallbackHandler(AWS_IoT_Client *pClient, void *data);      
void aws_iot_task(void *arg);

#endif //__AWS__
