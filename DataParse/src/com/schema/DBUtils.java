package schema;

import org.javalite.activejdbc.Base;

import com.owlike.genson.Genson;

public class DBUtils {
	static Genson gen = new Genson();
	public static void main(String[] args) {
		Base.open("com.mysql.jdbc.Driver","jdbc:mysql://localhost:3306/lb","root","");
		Test t = Test.findById(1);
		System.out.println(gen.serialize(t));
	}
}
