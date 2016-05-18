package officialMap;

import java.util.List;

import com.fasterxml.jackson.annotation.JsonProperty;

public class MapResponse {

	private Response response;
	private String status;
	public Response getResponse() {
		return response;
	}
	public void setResponse(Response response) {
		this.response = response;
	}
	public String getStatus() {
		return status;
	}
	public void setStatus(String status) {
		this.status = status;
	}
}

class Response{
	private Term term;
	private Data data;
	private Parameters parameters;
	private PointInfo pointinfo;
	public Term getTerm() {
		return term;
	}
	public void setTerm(Term term) {
		this.term = term;
	}
	public Data getData() {
		return data;
	}
	public void setData(Data data) {
		this.data = data;
	}
	public Parameters getParameters() {
		return parameters;
	}
	public void setParameters(Parameters parameters) {
		this.parameters = parameters;
	}
	public PointInfo getPointinfo() {
		return pointinfo;
	}
	public void setPointinfo(PointInfo pointinfo) {
		this.pointinfo = pointinfo;
	}
}

class Term{
	String value;

	public String getValue() {
		return value;
	}

	public void setValue(String value) {
		this.value = value;
	}
}

class Data{
	String curpage;
	String pagecount;
	String curresult;
	List<Feature> feature;
	String resultcount;
	public String getCurpage() {
		return curpage;
	}
	public void setCurpage(String curpage) {
		this.curpage = curpage;
	}
	public String getPagecount() {
		return pagecount;
	}
	public void setPagecount(String pagecount) {
		this.pagecount = pagecount;
	}
	public String getCurresult() {
		return curresult;
	}
	public void setCurresult(String curresult) {
		this.curresult = curresult;
	}
	public List<Feature> getFeature() {
		return feature;
	}
	public void setFeature(List<Feature> feature) {
		this.feature = feature;
	}
	public String getResultcount() {
		return resultcount;
	}
	public void setResultcount(String resultcount) {
		this.resultcount = resultcount;
	}
}
class Feature{
	Detail detail;
	String matchrank;
	String alias;
	
	@JsonProperty(value = "Style")  
	Style style;
	
	String caption;
	String cpid;
	String type;
	String id;
	Bounds bounds;
	String level;
	Points points;
	String dataid;
	String clustering;
	public Detail getDetail() {
		return detail;
	}
	public void setDetail(Detail detail) {
		this.detail = detail;
	}
	public String getMatchrank() {
		return matchrank;
	}
	public void setMatchrank(String matchrank) {
		this.matchrank = matchrank;
	}
	public String getAlias() {
		return alias;
	}
	public void setAlias(String alias) {
		this.alias = alias;
	}
	public Style getStyle() {
		return style;
	}
	public void setStyle(Style style) {
		this.style = style;
	}
	public String getCaption() {
		return caption;
	}
	public void setCaption(String caption) {
		this.caption = caption;
	}
	public String getCpid() {
		return cpid;
	}
	public void setCpid(String cpid) {
		this.cpid = cpid;
	}
	public String getType() {
		return type;
	}
	public void setType(String type) {
		this.type = type;
	}
	public String getId() {
		return id;
	}
	public void setId(String id) {
		this.id = id;
	}
	public Bounds getBounds() {
		return bounds;
	}
	public void setBounds(Bounds bounds) {
		this.bounds = bounds;
	}
	public String getLevel() {
		return level;
	}
	public void setLevel(String level) {
		this.level = level;
	}
	public Points getPoints() {
		return points;
	}
	public void setPoints(Points points) {
		this.points = points;
	}
	public String getDataid() {
		return dataid;
	}
	public void setDataid(String dataid) {
		this.dataid = dataid;
	}
	public String getClustering() {
		return clustering;
	}
	public void setClustering(String clustering) {
		this.clustering = clustering;
	}
	
}
class Points{
	String levels;
	String type;
	String txt;
	public String getLevels() {
		return levels;
	}
	public void setLevels(String levels) {
		this.levels = levels;
	}
	public String getType() {
		return type;
	}
	public void setType(String type) {
		this.type = type;
	}
	public String getTxt() {
		return txt;
	}
	public void setTxt(String txt) {
		this.txt = txt;
	}
	
}
class Bounds{
	String maxx;
	String maxy;
	String minx;
	String miny;
	public String getMaxx() {
		return maxx;
	}
	public void setMaxx(String maxx) {
		this.maxx = maxx;
	}
	public String getMaxy() {
		return maxy;
	}
	public void setMaxy(String maxy) {
		this.maxy = maxy;
	}
	public String getMinx() {
		return minx;
	}
	public void setMinx(String minx) {
		this.minx = minx;
	}
	public String getMiny() {
		return miny;
	}
	public void setMiny(String miny) {
		this.miny = miny;
	}
}
class Style{
	String id;

	public String getId() {
		return id;
	}

	public void setId(String id) {
		this.id = id;
	}
}
class Detail{
	String phone;
	String category;
	String county;
	String address;
	String subcategory;
	String province;
	String poidesc;
	String city;
	public String getPhone() {
		return phone;
	}
	public void setPhone(String phone) {
		this.phone = phone;
	}
	public String getCategory() {
		return category;
	}
	public void setCategory(String category) {
		this.category = category;
	}
	public String getCounty() {
		return county;
	}
	public void setCounty(String county) {
		this.county = county;
	}
	public String getAddress() {
		return address;
	}
	public void setAddress(String address) {
		this.address = address;
	}
	public String getSubcategory() {
		return subcategory;
	}
	public void setSubcategory(String subcategory) {
		this.subcategory = subcategory;
	}
	public String getProvince() {
		return province;
	}
	public void setProvince(String province) {
		this.province = province;
	}
	public String getPoidesc() {
		return poidesc;
	}
	public void setPoidesc(String poidesc) {
		this.poidesc = poidesc;
	}
	public String getCity() {
		return city;
	}
	public void setCity(String city) {
		this.city = city;
	}
	
}
class Parameters{
	String range;
	String what;
	String resultTypes;
	String exact;
	public String getRange() {
		return range;
	}
	public void setRange(String range) {
		this.range = range;
	}
	public String getWhat() {
		return what;
	}
	public void setWhat(String what) {
		this.what = what;
	}
	public String getResultTypes() {
		return resultTypes;
	}
	public void setResultTypes(String resultTypes) {
		this.resultTypes = resultTypes;
	}
	public String getExact() {
		return exact;
	}
	public void setExact(String exact) {
		this.exact = exact;
	}
	
}

class PointInfo{
	String level;
	String countincity;
	String smallpoint;
	String catetype;
	String iscategory;
	String city;
	public String getLevel() {
		return level;
	}
	public void setLevel(String level) {
		this.level = level;
	}
	public String getCountincity() {
		return countincity;
	}
	public void setCountincity(String countincity) {
		this.countincity = countincity;
	}
	public String getSmallpoint() {
		return smallpoint;
	}
	public void setSmallpoint(String smallpoint) {
		this.smallpoint = smallpoint;
	}
	public String getCatetype() {
		return catetype;
	}
	public void setCatetype(String catetype) {
		this.catetype = catetype;
	}
	public String getIscategory() {
		return iscategory;
	}
	public void setIscategory(String iscategory) {
		this.iscategory = iscategory;
	}
	public String getCity() {
		return city;
	}
	public void setCity(String city) {
		this.city = city;
	}
	
}