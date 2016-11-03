
public class DataController {

	// the addresses are currently organized by states
	// array order: bugerking, mcdonalds, pizzahut, wendys
	JunkFood[] jFoods;
	// array order: traderjoes, wholefoods
	HealthFood[] hFoods;

	
	DataController()
	{

		jFoods = new JunkFood[4];
		hFoods = new HealthFood[2];
		
		jFoods[0] = new JunkFood("src/Data/burgerking.xlsx");
		jFoods[1] = new JunkFood("src/Data/mcdonalds.xlsx");
		jFoods[2] = new JunkFood("src/Data/pizzahut.xlsx");
		jFoods[3] = new JunkFood("src/Data/wendys.xlsx");
		
		hFoods[0] = new HealthFood("src/Data/traderjoes.xlsx");
		hFoods[1] = new HealthFood("src/Data/wholefoods.xlsx");
	}
	
	JunkFood getJunk(int location)
	{
		return jFoods[location];
	}
	
	HealthFood getHealth(int location){
		return hFoods[location];
	}
	

}
