============================================================================================================

	All Database Functions

============================================================================================================
	
	Inhalt
	------	
	- BackUp.java:		--> Funktionalit�t, um ein Backup aller Karten auf einen externen MySql Server
							zu machen. Funktion: backUp
	- Categories.java:	--> Funktionalit�t um Kategorien zu erstellen, l�schen, abzufragen, usw.
	- Config.java:		--> Konfig Datenbank, welche Token und weitere Konfiginformationen enth�lt
	- Database.java:	--> Funktionalit�t, alle Karten abzuspeichern, zu l�schen, bearbeiten und sie
							in Form einer ArrayList<String[]> abzurufen
	- Doors.java:		--> Enth�lt die Funktionalit�t um T�ren zu erstellen und zu bearbeiten
	- Score.java:		--> Speichert, den Score, welcher von Yanis genutzt wird um sp�ter Infos �ber
							den Spieler zu erhalten, sowie Statistiken
	- Usercards.java:	--> Unused by Joel
	- Userlogin.java:	--> Unused by Joel
	
	Abh�ngigkeiten
	--------------
	- In diversen Models (DoorModel, CardModel, Usermodel) werden die Funktionen aus dem package 
	database verwendet. Die Funktionen doAction werden f�r Updates / Erstellen von Datens�tzen 
	verwendet, und getData um die Daten einer bestimmten Tabelle zu holen.
	- Dieses package verwendet diverse Globals, Enviromentfunktionen und Debugger Funktionen
	gr�sstenteils die debug.Debugger.out() Funktion
	
============================================================================================================

	HowTo
	-----
	- Die meisten Funktionen werden nicht direkt verwendet, sondern �ber die Models
	- Um die Funktionen zu verwenden --> database.KLASSE.FUNKTION(PARAMETER);
			
============================================================================================================

	Bei Fragen zur Datenbank wendet euch an Roger Schneiter und f�r Fragen zum Model an Miro/Hugo
	