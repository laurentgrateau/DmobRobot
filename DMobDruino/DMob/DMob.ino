#include <SoftwareSerial.h>

#include <URMSerial.h>

/*
DMob Arduino Program
 */



// The measurement we're taking
#define DISTANCE 1
#define TEMPERATURE 2
#define ERROR 3
#define NOTREADY 4
#define TIMEOUT 5

//Standard PWM DC control
#define E1 5     //M1 Speed Control
#define E2 6     //M2 Speed Control
#define M1 4    //M1 Direction Control
#define M2 7    //M1 Direction Control

URMSerial urm;

// ----------------------------
// -- Motors Function
// ----------------------------
void stop(void)                    //Stop
{
  digitalWrite(E1,LOW);  
  digitalWrite(E2,LOW);     
}  
void advance(char a,char b)          //Move forward
{
  analogWrite (E1,a);      //PWM Speed Control
  digitalWrite(M1,HIGH);   
  analogWrite (E2,b);   
  digitalWrite(M2,HIGH);
} 
void back_off (char a,char b)          //Move backward
{
  analogWrite (E1,a);
  digitalWrite(M1,LOW);  
  analogWrite (E2,b);   
  digitalWrite(M2,LOW);
}
void turn_L (char a,char b)             //Turn Left
{
  analogWrite (E1,a);
  digitalWrite(M1,LOW);   
  analogWrite (E2,b);   
  digitalWrite(M2,HIGH);
}
void turn_R (char a,char b)             //Turn Right
{
  analogWrite (E1,a);
  digitalWrite(M1,HIGH);   
  analogWrite (E2,b);   
  digitalWrite(M2,LOW);
}

// ----------------------------
// -- Sensors Function
// ----------------------------
int value; // This value will be populated
int getMeasurement(int measureType)
{
  // Request a distance reading from the URM37
  switch(urm.requestMeasurementOrTimeout(measureType, value)) // Find out the type of request
  {
  case DISTANCE: // Double check the reading we recieve is of DISTANCE type
    //    Serial.println(value); // Fetch the distance in centimeters from the URM37
    return value;
    break;
  case TEMPERATURE:
    return value;
    break;
  case ERROR:
    Serial.println("Error");
    break;
  case NOTREADY:
    Serial.println("Not Ready");
    break;
  case TIMEOUT:
    Serial.println("Timeout");
    break;
  } 

  return -1;
}

// ----------------------------
// -- Setup Function
// ----------------------------
void setup() {
  int i;
 // for(i=4;i<=7;i++)
  //  pinMode(i, OUTPUT); 
  Serial.begin(19200);                  // Sets the baud rate to 9600
  urm.begin(3,2,19200);                 // RX Pin, TX Pin, Baud Rate
  Serial.println("DMob");   // Shameless plug 
}
// ----------------------------
// -- Main Loop
// ----------------------------  
void loop()
{
  if(Serial.available()){
    char val = Serial.read();
    if(val != -1)
    {
      switch(val)
      {
      case 'f'://Move Forward
        advance (255,255);   //move forward in max speed
        break;
      case 'b'://Move Backward
        back_off (255,255);   //move back in max speed
        break;
      case 'l'://Turn Left
        turn_L (100,100);
        break;      
      case 'r'://Turn Right
        turn_R (100,100);
        break;
      case 'd':
        Serial.print("Sensor:D:");
        Serial.println(getMeasurement(DISTANCE));
        break;
      case 't':
        Serial.print("Sensor:T:");
        Serial.println(getMeasurement(TEMPERATURE));
        break;

      case 'x':
        stop();
        break;
      }
    }
    else stop(); 
  }
}










