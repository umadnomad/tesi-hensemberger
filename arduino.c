#
# Copyright (C) 2018 - Riccardo Finazzi, ITIS P.Hensemberger

# This program is free software: you can redistribute it and/or modify
# it under the terms of the GNU General Public License as published by
# the Free Software Foundation, either version 3 of the License, or
# (at your option) any later version.
# 
# This program is distributed in the hope that it will be useful,
# but WITHOUT ANY WARRANTY; without even the implied warranty of
# MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.  See the
# GNU General Public License for more details.
# 
# You should have received a copy of the GNU General Public License
# along with this program.  If not, see <http://www.gnu.org/licenses/>
# 

#include <toneAC2.h>                                          // BUZZER
#include <MFRC522.h>                                          // RFID
#include <Servo.h>                                            // SERVO

#define RPI_STEP_1      40                                    // RASPBERRY
#define RPI_STEP_2      41                                    // RASPBERRY
#define RPI_STEP_3      42                                    // RASPBERRY

#define GOOD_NOTE       1000                                  // BUZZER
#define BAD_NOTE        840                                   // BUZZER
#define BUZZER_PIN_1    11                                    // BUZZER
#define BUZZER_PIN_2    12                                    // BUZZER

#define DOOR_OPEN_PIN   3                                     // RELAY
#define DOOR_CLOSE_PIN  4                                     // RELAY

#define CARD_UID        4294935243                            // RFID
#define RST_PIN         48                                    // RFID
#define SS_PIN          53                                    // RFID
MFRC522 mfrc522(SS_PIN, RST_PIN);                             // RFID

#define LOCK_PIN        2                                     // SERVO
#define LOCK_CLOSE      140                                   // SERVO
#define LOCK_OPEN       25                                    // SERVO
Servo servo;                                                  // SERVO

void setup() {
  pinMode(RPI_STEP_1, OUTPUT);                                // RASPBERRY
  pinMode(RPI_STEP_2, OUTPUT);                                // RASPBERRY
  pinMode(RPI_STEP_3, INPUT);                                 // RASPBERRY

  pinMode(DOOR_OPEN_PIN, OUTPUT);                             // RELAY
  digitalWrite(DOOR_OPEN_PIN, HIGH);                          // RELAY

  pinMode(DOOR_CLOSE_PIN, OUTPUT);                            // RELAY
  digitalWrite(DOOR_CLOSE_PIN, HIGH);                         // RELAY

  SPI.begin();                                                // RFID
  mfrc522.PCD_Init();                                         // RFID

  initLock();
  // Serial.begin(9600);
}

byte stateMachine = 0;                                        // STATE MACHINE

void loop() {

  delay(500);                                                 // POWER SAVE

  // RFID
  if (mfrc522.PICC_IsNewCardPresent()) {
    unsigned long uid = getID();
    if (uid != -1) {
      if (uid == 4294935243) {

        // Serial.println("true");
        
        if (stateMachine == 0) {                              // STATE MACHINE
          stateMachine = 1;                                   // STATE MACHINE
          playNote(GOOD_NOTE, 1000);                          // BUZZER
          digitalWrite(RPI_STEP_1, HIGH);
          delay(5000);
          digitalWrite(RPI_STEP_1, LOW);
          return;
        }

        if (stateMachine == 1) {                              // STATE MACHINE
          stateMachine = 2;                                   // STATE MACHINE
          playNote(GOOD_NOTE, 1000);                          // BUZZER
          digitalWrite(RPI_STEP_2, HIGH);
          delay(5000);
          digitalWrite(RPI_STEP_2, LOW);
          return;
        }

        if (stateMachine == 3) {                              // STATE MACHINE
          preM();
          closeDoor();
          postM();
          closeLock();

          stateMachine = 0;                                   // STATE MACHINE
          return;
        }
      }
    }
  }

  if (stateMachine == 2 && digitalRead(RPI_STEP_3) == LOW) {  // STATE MACHINE
    stateMachine = 3;                                         // STATE MACHINE
    preM();
    openLock();
    postM();
    openDoor();
  }
}

// BUZZER
void playNote(int note, int duration) {
  toneAC2(BUZZER_PIN_1, BUZZER_PIN_2, note, duration);
}

// BUZZER
void preM() {
  playNote(BAD_NOTE, 200);
  delay(200);
  playNote(BAD_NOTE, 200);
  delay(200);
  playNote(BAD_NOTE, 200);
  delay(200);
}

// BUZZER
void postM() {
  delay(1000);
  playNote(GOOD_NOTE, 1000);
}

// RFID
unsigned long getID() {

  if ( ! mfrc522.PICC_ReadCardSerial()) {
    return -1;
  }
  unsigned long hex_num;
  hex_num =  mfrc522.uid.uidByte[0] << 24;
  hex_num += mfrc522.uid.uidByte[1] << 16;
  hex_num += mfrc522.uid.uidByte[2] <<  8;
  hex_num += mfrc522.uid.uidByte[3];
  mfrc522.PICC_HaltA();
  return hex_num;
}

// SERVO
void initLock() {
  servo.attach(LOCK_PIN);
  servo.write(LOCK_OPEN);
  delay(300);
  servo.detach();
  servo.attach(LOCK_PIN);
  servo.write(LOCK_CLOSE);
  delay(300);
  servo.detach();
  delay(500);
  playNote(GOOD_NOTE, 200);
}

// SERVO
void openLock() {
  servo.attach(LOCK_PIN);
  servo.write(LOCK_OPEN);
  delay(300);
  servo.detach();
}

// SERVO
void closeLock() {
  servo.attach(LOCK_PIN);
  servo.write(LOCK_CLOSE);
  delay(300);
  servo.detach();
}

// RELAY
void openDoor() {
  digitalWrite(DOOR_OPEN_PIN, LOW);
  delay(1000);
  digitalWrite(DOOR_OPEN_PIN, HIGH);
}

// RELAY
void closeDoor() {
  digitalWrite(DOOR_CLOSE_PIN, LOW);
  delay(100);
  digitalWrite(DOOR_CLOSE_PIN, HIGH);
}