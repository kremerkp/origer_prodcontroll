package lu.origer.serviceagree.models.sampleData;

import java.util.Date;

import lu.origer.serviceagree.models.contract.BuildingSite;
import lu.origer.serviceagree.models.main.Users;

public class GenerateSampleData {
	
	public BuildingSite generateBuildingSite(Users user){
		BuildingSite bs = new BuildingSite(); 
		bs.setActive(true);
		bs.setCode("CODE123");
		bs.setCreateDate(new Date());
		bs.setCreateUser(user.getId());
		bs.setEditUser(user.getId());
		return bs;
	}

}
