package app.taxiAnytimeCustomer.Common;


/** @info
 * Σε αυτο το αρχειο γινετε αρχικοποιηση των global μεταβλητων.
 * Γινετε χρηση του Singleton pattern για να εχουμε μοναδικη αναφορα στην 
 * συγκεκριμενη κλαση
 *
 */
public class globalVariables {
	
		//MQTT BROKER 
		private String MQTT_IP_ADDRESS ;
		private int MQTT_PORT;
	
		//WAMP SERVER
		private    String IP_ADDRESS ;
		private    int HTTP_PORT;
		private    String BASE_PATH;
		
		
		//instance
		private static globalVariables instance = null;
		
		public globalVariables() {
			
			MQTT_IP_ADDRESS = "192.168.2.100";
			MQTT_PORT = 1883;
			 
			IP_ADDRESS = "192.168.2.100";
			HTTP_PORT = 80;
			BASE_PATH = "http://"+IP_ADDRESS+"/taxiAnytime_server";
			                                 
		}
		

		public static globalVariables getInstance() {
			if (instance == null)
				instance = new globalVariables();	
			return instance;
		}
		
		
		public String getBASE_PATH() {
			return BASE_PATH;
		}
		
		public int getHTTP_PORT() {
			return HTTP_PORT;
		}
		
		public String getIP_ADDRESS() {
			return IP_ADDRESS;
		}
		
		public String getMQTT_IP_ADDRESS() {
			return MQTT_IP_ADDRESS;
		}
		
		public int getMQTT_PORT() {
			return MQTT_PORT;
		}
		


}
