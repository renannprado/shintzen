package test;

public class SinTest {
 
	
	
	
	public static void main(String[] args) {
		
		int size = 20;
		
		float stepSize = 0.1f;
		
		for( float step=0; step<100; step += stepSize ) {
			System.out.println( "X:" + Math.sin(step) * size );
		}
	}
}
