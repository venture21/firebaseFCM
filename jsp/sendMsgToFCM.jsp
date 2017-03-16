<%@ page language="java" contentType="text/html; charset=UTF-8" pageEncoding="UTF-8"%>
<%@ page import="java.util.*, java.security.*, java.io.*, java.net.*" %>
<%
String result = "";

// 1. fcm 서버정보 세팅
String fcm_url = "https://fcm.googleapis.com/fcm/send";
String content_type = "application/json";
String server_key = "AAAAMGPTKAI:APA91bG9M1Er0a4H1fWAI31oUWB8TGwQzgquhTP6uMCp4sVipe6K5DrnGnmBWPzXJerumJyMQ_O4YaCUeCtSr4eTKfWWSrHGln15_aJWfrchplg-uEzBjpMr_ypf8NfhCuvsSqtxyo90";

// 2. 메시지정보를 클라이언트(핸드폰)로부터 수신
request.setCharacterEncoding("utf-8");  // 요청값이 한글일 경우 처리

String to_token = request.getParameter("to_token");
String msg      = request.getParameter("msg");
String sender   = request.getParameter("sender");
String title    = " 보내는사람:"+sender;

// 줄 바꿈시 브라우저에서는 '\n' 이 실행되지 않으므로 <br>을 추가한다. 
out.print(" [msg] <br> "+msg+"<br>"); 
out.print("<br><br>"); 

// 3. fcm 서버로 메시지를 전송
// 3.1 수신한 메시지를 json 형태로 변경해준다.(다운스트림 메시지 구문 내용 참조)
String json_string = "{\"to\": \""+to_token+"\", \"notification\": { \"title\":\""+title+"\" , \"body\": \"" + msg + "\"}}";

out.print(" [json_string] <br> "+json_string+"<br>"); 
out.print("<br><br>"); 

// 3.2 HttpUrlConnection을 사용해서 FCM서버측으로 메시지를 전송한다

// a. 서버연결
URL url = new URL(fcm_url);
HttpURLConnection con = (HttpURLConnection) url.openConnection();

// b. Header 설정
con.setRequestMethod("POST");
con.setRequestProperty("Authorization","key="+server_key);
con.setRequestProperty("Content-Type",content_type);

// c. post 데이터(body)전송
con.setDoOutput(true);
OutputStream os = con.getOutputStream();
os.write(json_string.getBytes());
os.flush();
os.close();

// d.전송후 결과처리
int responseCode = con.getResponseCode();
if(responseCode == HttpURLConnection.HTTP_OK){ //code 200
	BufferedReader br = new BufferedReader(new InputStreamReader(con.getInputStream()));
	String dataLine ="";
	// 메시지를 한 줄씩 읽어서 result 변수에 담아두고
	while((dataLine = br.readLine()) != null){
		result = result + dataLine;
	}
	br.close();
}

out.print("RESULT:"+result);
%>