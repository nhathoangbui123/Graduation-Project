#ifndef PZEM004TV30_H
#define PZEM004TV30_H

#include "string.h"
#include <stdint.h>
#include "driver/uart.h"
#include "driver/gpio.h"

#define PZEM_DEFAULT_ADDR   0xF8
#define PZEM_BAUD_RATE      9600
#ifdef __cplusplus
extern "C" {
#endif
typedef struct
{
	uart_port_t uart_port;
	int tx_io_num;
	int rx_io_num;
} uart_data_t;
class pzem
{
    public:
        pzem(uint8_t addr);
        pzem(){};
        ~pzem();

        float voltage();
        float current();
        float power();
        float energy();
        float frequency();
        float pf();

        bool setAddress(uint8_t addr);
        uint8_t getAddress();
        uint8_t readAddress(bool update = false);

        bool setPowerAlarm(uint16_t watts);
        bool getPowerAlarm();

        bool resetEnergy();

        void search();

        void pzem_task(void *arg);
    private:
        uint8_t _addr;   // Device address

        bool _isConnected; // Flag set on successful communication
        uart_data_t * _uart_data;
        struct {
            float voltage;
            float current;
            float power;
            float energy;
            float frequency;
            float pf;
            uint16_t alarms;
        }  _currentValues; // Measured values

        uint64_t _lastRead; // Last time values were updated

        void init(uint8_t addr); // Init common to all constructors
        uint64_t millis();
        bool updateValues();    // Get most up to date values from device registers and cache them
        uint16_t receive(uint8_t *resp, uint16_t len); // Receive len bytes into a buffer

        bool sendCmd8(uint8_t cmd, uint16_t rAddr, uint16_t val, bool check, uint16_t slave_addr=0xFFFF); // Send 8 byte command

        void setCRC(uint8_t *buf, uint16_t len);           // Set the CRC for a buffer
        bool checkCRC(const uint8_t *buf, uint16_t len);   // Check CRC of buffer

        uint16_t CRC16(const uint8_t *data, uint16_t len); // Calculate CRC of buffer
};
#ifdef __cplusplus
}
#endif
// #ifdef __cplusplus
// extern "C" {
// #endif
// typedef struct
// {
// 	uart_port_t uart_port;
// 	int tx_io_num;
// 	int rx_io_num;
// } uart_data_t;

// static const int RX_BUF_SIZE = 1024;

// typedef struct {
//     float voltage;
//     float current;
//     float power;
//     float energy;
//     float frequency;
//     float pf;
//     uint16_t alarms;
// } power_meansuare_t; // Measured values

// void PZEM004Tv30_Init(uart_data_t *uart_data, uint8_t addr);

// power_meansuare_t* meansures();
// float voltage();
// float current();
// float power();
// float energy();
// float frequency();
// float pf();


// bool setAddress(uint8_t addr);
// uint8_t getAddress();

// bool setPowerAlarm(uint16_t watts);
// bool getPowerAlarm();

// bool resetEnergy();

// // void search();

// void init(uint8_t addr); // Init common to all constructors

// bool updateValues();    // Get most up to date values from device registers and cache them
// uint16_t recieve(uint8_t *resp, uint16_t len); // Receive len bytes into a buffer

// bool sendCmd8(uint8_t cmd, uint16_t rAddr, uint16_t val, bool check, uint16_t slave_addr); // Send 8 byte command

// void setCRC(uint8_t *buf, uint16_t len);           // Set the CRC for a buffer
// bool checkCRC(const uint8_t *buf, uint16_t len);   // Check CRC of buffer

// uint16_t CRC16(const uint8_t *data, uint16_t len); // Calculate CRC of buffer
// void pzem_task(void *arg);
// #ifdef __cplusplus
// }
// #endif
#endif // PZEM004TV30_H
