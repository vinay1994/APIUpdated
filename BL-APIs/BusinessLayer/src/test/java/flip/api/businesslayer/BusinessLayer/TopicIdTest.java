package flip.api.businesslayer.BusinessLayer;

import org.json.JSONArray;
import org.json.JSONObject;
import org.testng.annotations.Test;
import util.Xls_Reader;

import com.jayway.restassured.response.Response;
public class TopicIdTest extends App{
	String Base_url="https://stgtoc.fliplearn.com:8087/";
	//https://stgtoc.fliplearn.com:8087/v1/resource?topicId=1
	Xls_Reader xls;
	String topicId,resourceId,resourceName,type,title,wouzaUrl;

	@Test
	public void hitTopicIds(){
		int counter=2;
		Response response = null;
		JSONArray jsonArray = null;
		JSONObject jsonObject;
		xls=new Xls_Reader();
		for(int i=2;i<xls.getRowCount("topicId");i++){
			topicId=xls.getCellData("topicId", "topic_Id", i);
			System.out.println("\nRunning for topic id-->> "+topicId);
			response = hitGetRequest("satya.staging", Base_url+"v1/resource?topicId="+topicId+"");
			try {
				jsonObject = new JSONObject(response.body().asString());
				jsonArray = jsonObject.getJSONArray("response");
			} catch (Exception e) {}
			for(int j=0;j<jsonArray.length();j++){
				xls.setCellData("ResourceData", "topicId", counter, topicId);
				xls.setCellData("ResourceData", "resourceSize", counter, String.valueOf(jsonArray.length()));
				xls.setCellData("ResourceData", "resourceId", counter, jsonArray.getJSONObject(j).get("resourceId").toString());
				xls.setCellData("ResourceData", "resourceName", counter, jsonArray.getJSONObject(j).get("resourceName").toString());
				xls.setCellData("ResourceData", "type", counter, jsonArray.getJSONObject(j).get("type").toString());
				xls.setCellData("ResourceData", "title", counter, jsonArray.getJSONObject(j).get("title").toString());
				xls.setCellData("ResourceData", "wouzaUrl", counter, jsonArray.getJSONObject(j).get("wouzaUrl").toString());
				counter++;
			}
			//System.out.println(response.asString());
		}
	}

}
