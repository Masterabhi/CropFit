#include <SoftwareSerial.h>

SoftwareSerial BTserial(0, 1); // RX | TX

//10,11
#include <SimpleDHT.h>

// for DHT11, 
//      VCC: 5V or 3V
//      GND: GND
//      DATA: 2
int pinDHT11 = 2;
SimpleDHT11 dht11;
///////Soil moisture
int sensor_pin = A0; 
int output_value ;
//////PHsensor
const int analogInPin = A1; 
int sensorValue = 0; 
unsigned long int avgValue; 
float b;
int buf[10],temp;


void setup() {
  Serial.begin(9600);
  Serial.begin(9600);
}

void loop() {
  


  // start working...
  BTserial.println("=================================");
  BTserial.println("Sample DHT11...");
  
  // read without samples.
  byte temperature = 0;
  byte humidity = 0;
  int err = SimpleDHTErrSuccess;
  if ((err = dht11.read(pinDHT11, &temperature, &humidity, NULL)) != SimpleDHTErrSuccess) {
    Serial.print("Read DHT11 failed, err="); 
    Serial.println(err);
    delay(1000);
    return;
  }
  ////soil moisture
   output_value= analogRead(sensor_pin);
  output_value = map(output_value,550,0,0,100);
  ////Soil Moisture End

  ////Ph start
  for(int i=0;i<10;i++) 
 { 
  buf[i]=analogRead(analogInPin);
  delay(10);
 }
 for(int i=0;i<9;i++)
 {
  for(int j=i+1;j<10;j++)
  {
   if(buf[i]>buf[j])
   {
    temp=buf[i];
    buf[i]=buf[j];
    buf[j]=temp;
   }
  }
 }
 avgValue=0;
 for(int i=2;i<8;i++)
 avgValue+=buf[i];
 float pHVol=(float)avgValue*5.0/1024/6;
 float phValue = -5.70 * pHVol + 21.34;
  /////Ph end
  BTserial.print("Sample OK: ");
  Serial.print((int)temperature);
  //BTserial.println(" *C");
 
  Serial.print(","); 
  Serial.print((int)humidity); 
  //BTserial.println(" H");
 

  Serial.print(",");
  Serial.print((int)output_value);
//BTserial.println(" *C");
 
  Serial.print(","); 
  Serial.print((int)temperature);
  //BTserial.println(" *C");
 
   Serial.print(","); 
   Serial.print((int)phValue);
  //BTserial.println(" *C");
  
 
  Serial.println(";"); 
  
  //BTserial.print((int)humidity); 
  //BTserial.print(",");
  //BTserial.print((int)humidity); 
  //BTserial.print(",");
  //BTserial.print((int)humidity); 
  //BTserial.print(",");

 
  
  // DHT11 sampling rate is 1HZ.
  delay(1500);
}
