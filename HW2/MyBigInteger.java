/*
 *   STUDENT NAME      :  
 *   STUDENT ID        :
 *   
 *   If you have any issues that you wish the T.A.s to consider, then you
 *   should list them here.   If you discussed on the assignment in depth 
 *   with another student, then you should list that student's name here.   
 *   We insist that you each write your own code.   But we also expect 
 *   (and indeed encourage) that you discuss some of the technical
 *   issues and problems with each other, in case you get stuck.    

 *   
 */

import java.util.ArrayList;

public class MyBigInteger  implements Comparable<MyBigInteger> {   //  We will cover what "implements Comparable" means later

	/*
	 *   
	 *  If the number has N digits,  then it is represented is a polynomial:
	 *  coefficients[N-1] base^{N-1} + ...  coefficients[1] base^{1} +  coefficients[0] 
	 *  where base has a particular value and the coefficients are in {0, 1, ...  base - 1}
	 *  
	 *  For any base and any positive integer, the representation of that positive 
	 *  integer as a sum of powers of that base is unique.  
	 *  
	 *  We require that the coefficient of the largest power is non-zero.  
	 *  For example,  '354' is a valid representation (which we call "three hundred fifty four") 
	 *  but '0354' is not.  
	 * 
	 */
	
	private int	base;       

	private ArrayList<Integer>  coefficients;

	//  Constructors

	MyBigInteger(int base){
		
		//  If no string argument is given, then this constructor constructs an empty list of coefficients.
		
		this.base = base;
		coefficients = new ArrayList<Integer>();
	}

	/*       
	 *   The constructor builds a ArrayList of Integer objects where the integers are in [0,base), i.e.  0, 1, ..., base - 1.
	 *   Note that the argument numberAsString only represents a base 10 number when the base is 10.
	 */
	
	
	
	MyBigInteger(String numberAsString,  int base) throws Exception{
		int i;
		this.base = base;
		coefficients = new ArrayList<Integer>();
		if ((base < 2) || (base > 10)){
			System.out.println("constructor error:  base must be between 2 and 10");
			throw new Exception();
		}

		int len = numberAsString.length();
		for (int indx = 0; indx < len; indx++){  
			i = numberAsString.charAt(indx);
			
			/*
			 *  ascii value of symbol '0' is 48, symbol '1' is 49, etc.
			 *  So,  to get the numerical value of '2',  we subtract 
             *  the character value of '0' (48) from the character value of '2' (50).
			 */
            
			if ( (i >= 48) && (i - 48 < base))   				
				coefficients.add(0,  Integer.valueOf(i-48) );
			else{
				System.out.println("constructor error:  all coefficients should be non-negative and less than base");
				throw new Exception();
			}			
		}
	}
	
	/*
	 *   This Constructor allows the input to be an int.   The maximum value can therefore be 2^31-1.     
	 */
	public static void main(String[] args) {
		MyBigInteger i = new MyBigInteger(10);
	}
	
	MyBigInteger(int i,  int base) throws Exception{
		this.base = base;
		coefficients = new ArrayList<Integer>();
		
		if (i < 0) {
			System.out.println("constructor error: we are only dealing with non-negative integers");
			throw new Exception();
		}
		else if (i == 0)
			coefficients.add( Integer.valueOf(0) );
		else {
			int m = i;
			while (m > 0) {
				coefficients.add( Integer.valueOf(m % base) );
				m /= base;
			}
		}
	}
	
	public int getBase()
	{
		return base;
	}
	
	//  normally this would be private, but I make it public in case the Grader needs it.
	
	public ArrayList<Integer>  getCoefficients()
	{
		return coefficients;
	}

	/*
	 *   The plus method computes this.plus(b), that is, a+b where 'this' is a.
	 */

	public MyBigInteger plus( MyBigInteger  second) throws Exception{
				
		//  initialize the result as an empty list of coefficients
		
		MyBigInteger result = new MyBigInteger( this.base );

		if (this.base != second.base){
			System.out.println("ERROR: bases must be the same in an addition");
			throw new Exception();
		}

		/*   
		 * The plus method must not affect the numbers themselves. 
		 * So let's just work  with a copy (a clone) of the numbers. 
		 */

		MyBigInteger  firstClone  = this.clone();
		MyBigInteger  secondClone = second.clone();

		/*
		 *   If the two numbers have a different polynomial order    
		 *   then pad the smaller one with zero coefficients.
		 */
		
		int   diff = firstClone.coefficients.size() - second.coefficients.size();
		while (diff < 0){  // second is bigger                                                     		
			firstClone.coefficients.add(0);			        
			diff++;
		}
		while (diff > 0){  //  this is bigger
			secondClone.coefficients.add(0);
			diff--;
		}

		/*
		 *   'firstClone' and 'secondClone' have the same size.  We add the coefficients
		 *   term by term.    If the last coefficient yields a carry, then we add 
         *   one more term with the carry.	
         */
		
		int tmp;
		int carry = 0;
		
		for (int i=0; i < firstClone.coefficients.size(); i++  ){
			tmp = firstClone.coefficients.get(i) + secondClone.coefficients.get(i) + carry;
			result.coefficients.add( tmp % base ) ;
			carry = tmp / base; 
		}
		if (carry > 0)
			result.coefficients.add(carry);    //   carry always would have the value 1 in this case.
		
		return result;		
	}

	/*   
	 *    Slow multiplication algorithm.
	 *    'this' is the multiplicand i.e. a*b = a+a+a+...+a (b times) where a is multiplicand and b is multiplier
	 */
	
	public MyBigInteger slowTimes( MyBigInteger  multiplier) throws Exception{

		MyBigInteger prod  = new MyBigInteger(0, this.base);
		MyBigInteger one   = new MyBigInteger(1, this.base);  // This is used to increment the counter.
		
		for (MyBigInteger counter = new MyBigInteger(0, this.base);  counter.compareTo(multiplier) < 0;  counter = counter.plus(one) ){
			prod = prod.plus(this);
		}
		return prod;
	}
	
	/*
	 *    The times method is NOT be the same as what you learned in grade school which 
	 *    uses a temporary 2D table with space proportional to the square of 
	 *    the number of coefficients in the operands, i.e.  N^2.   The method here accumulates 
	 *    the result by adding each row of the table as it is computed. 
	 *    This method uses space that is proportional to the number of coefficients N.    
	 *    The multiplication algorithm will still take time O(N^2) however.   
	 *    
	 *    The  method computes this.times(b) where 'this' is a.
	 */
	
	public MyBigInteger times( MyBigInteger multiplicand) throws Exception{
		
		//  initialize product as an empty list of coefficients
		
		MyBigInteger product = new MyBigInteger( this.base );

		if (this.base != multiplicand.base){
			System.out.println("ERROR: bases must be the same in a multiplication");
			throw new Exception();
		}
		
		/*
		 *           multiplicand
		 *          x  multiplier  (this)
		 *        ---------------
		 *            
		 *   Note we use helper methods.
		 */

		MyBigInteger row;
		for (int i=0; i < this.coefficients.size(); i++){
			row = multiplicand.timesSingleDigit( this.coefficients.get(i) ).timesBaseToThePower(i);
			product = product.plus( row  );
		}
		
		// Remove leading zeros
		while (product.coefficients.size() > 0 && product.coefficients.get(product.coefficients.size()-1) == 0)
			product.coefficients.remove(product.coefficients.size()-1);
		
		return product;
	}

	/*
	 *    'this' (the caller) will be the multiplicand.   
	 */
	
	private MyBigInteger timesSingleDigit( int  singleDigit) throws Exception{

		//  Assumes that 0 <= singleDigit < base. 
		
		int carry = 0;
		int tmp;

		//  initialize prod as an empty list of coefficients
	
		MyBigInteger prod = new MyBigInteger(this.base);

		//  multiply the single digit by each of the digits in this.coefficients
		//  This is like a row in the grade school calculation.
		
		for (int i=0; i < this.coefficients.size(); i++  ){
			tmp = this.coefficients.get(i) * singleDigit + carry;
			prod.coefficients.add( tmp % this.base ) ;
			carry = tmp / this.base; 
		}
		if (carry > 0)
			prod.coefficients.add(carry);
		return prod;
	}
	
	/*
	 *   The minus method computes this.minus(b) where 'this' is a, and a > b.
	 *   If a < b, then it throws an exception.
	 *   
	 *   The solution below uses variable names  first and second rather than a and b.
	 *   So we are computing first.minus(second)
	 */
	
	public MyBigInteger  minus(MyBigInteger second) throws Exception{

		//  initialize the result as an empty list of coefficients
		
		MyBigInteger  result = new MyBigInteger(this.base);

		if (this.base != second.base){
			System.out.println("ERROR: bases must be the same in a subtraction");
			throw new Exception();
		}
		/*
		 *    The minus method is not supposed to change the numbers. 
		 *    But the grade school algorithm sometimes requires us to "borrow"
		 *    from a higher coefficient to a lower one.   So we work
		 *    with a copy (a clone) instead.
		 */

		MyBigInteger  first = this.clone();

		//   Verify 'this' >= second. 
		 
		if (this.compareTo(second) < 0){
			System.out.println("Error: a.minus(b) requires that a > b");
			throw new Exception();
		}

		int i = 0;       //  coefficient position 
		int diffCoef;    //  compute the difference of two coefficients   this[i] - second[i]
	
		while (i < first.coefficients.size()){
		
			/*
			 *   Start from the least significant digit.
			 *   Check if 'second' has a term at this position. 
			 *   If yes, then need to take the difference at this position.    That's where the bulk of work is.
			 *   If no, then the coefficient at second is treated as 0 and just copy the i'th coefficient from first to the result. 
			 */
			
			if (i < second.coefficients.size()){
				
				diffCoef = first.coefficients.get(i) - second.coefficients.get(i);		

				if (diffCoef >= 0)
					result.coefficients.add(Integer.valueOf(diffCoef));
				else {
					/*
					 *   .. then we need to borrow from the next coefficient.
					 *   But if the next coefficient holds a '0', then we need a sequence of borrows.
					 *   
					 *   For example, suppose we are computing 30001 - 6.   Then we essentially treat
					 *   30001 as  11 + 90 + 900 + 9000 + 2000.     
					 *   So we would want to rewrite the list of coefficients as (11, 9, 9, 9, 2).
					 */
					int j = i;
					while (first.coefficients.get(j+1) == 0){
						first.coefficients.set(j+1, base-1);
						j++;
					}
					first.coefficients.set(j+1, Integer.valueOf( first.coefficients.get(j+1) - 1 ));

					//  Finally, compute the result for position i
					result.coefficients.add(Integer.valueOf(base + diffCoef));
				}
			}
			else
				result.coefficients.add(Integer.valueOf(first.coefficients.get(i)));
			i++;
		}

		/*  
		 *  In the case of say  100-98, we will end up with 002.  
		 *  Remove all the leading 0's of the result.
		 */
		
		while ((result.coefficients.size() > 1) & 
				(result.coefficients.get(result.coefficients.size()-1) == 0)){
			result.coefficients.remove(result.coefficients.size()-1);
		}
		return result;	
	}
	
	/*   
	 *    Slow division algorithm is repeated subtraction, mentioned in lecture 1.
	 */
	
	public MyBigInteger slowdividedBy( MyBigInteger  divisor) throws Exception{

		MyBigInteger one = new MyBigInteger(1,base);
		MyBigInteger quotient = new MyBigInteger(0,base);
		MyBigInteger remainder = this.clone();
		while ( remainder.compareTo(divisor) >= 0 ){
			remainder = remainder.minus(divisor);
			quotient = quotient.plus(one);
		}
		return quotient;
	}

	/*  
	 *  The dividedBy method divides 'this' by 'divisor' i.e. this.dividedBy(divisor)
	 *   It returns the quotient and ignores the remainder.
	 */
	
	public MyBigInteger dividedBy(MyBigInteger  divisor ) throws Exception{
		
		MyBigInteger zero = new MyBigInteger(0,this.base);
		MyBigInteger quotient = new MyBigInteger(this.base);
		MyBigInteger base = new MyBigInteger(this.base, this.base); 

	//If the bases are not equal then throw an exception
		if (this.base != divisor.base){
			throw new Exception("Your base must be the same as the divisior base");
		}
	//divisor is greater than the dividend
		if(this.compareTo(divisor) == -1){
			return zero;		
		}
	//If divides by zero
		if (divisor.compareTo(zero) == 0) {
			throw new ArithmeticException("Not allowed to divide by 0");
				}		
	//if divisor and dividend same size
		if(divisor.coefficients.size()==this.coefficients.size()) {
			MyBigInteger t = this.slowdividedBy(divisor);
			return t;
		}
		MyBigInteger a = new MyBigInteger(this.coefficients.get(this.coefficients.size()-1), this.base);
		MyBigInteger b = zero.clone();
		MyBigInteger c = this.clone();
		MyBigInteger Rem = new MyBigInteger(c.base);
		MyBigInteger Quo = new MyBigInteger(c.base);
		MyBigInteger dividend = new MyBigInteger(c.base);

	//Iterate through tempC (this) and repeat
			for(int i = c.coefficients.size()-2; i>-1; i--) {
				
				//finding the next digit at index i dividend = base*tempA + next
				MyBigInteger next = new MyBigInteger(c.coefficients.get(i),c.base);
				dividend = base.times(a).plus(next);
				
				MyBigInteger next2 = dividend.slowdividedBy(divisor);
				Rem = dividend.minus((divisor.times(next2)));
				Quo = base.times(b).plus(next2);
	
				a = Rem.clone();
				b = Quo.clone();
				quotient = b.clone();
			}
			return quotient;		
			}
	
	/*  The convert method converts between two bases that are each in {2, ..., 10}
	 *  The convert method specifies the new base that you are converting into.
	 *  The subtlety of the convert method is that it uses operations in this.base
	 *  and so if you are converting into a higher base then you have a problem
	 *  since the dividedBy method will produce a result in the this.base. 
	 */
	
	public MyBigInteger convert(int newBase) throws Exception {
		
		MyBigInteger remainder = this.clone();   
		MyBigInteger result = new MyBigInteger(newBase);

		//if base1 = base2 return 
				if(this.base == newBase) {
					return this;
				}
				
		//if this.base < newBase, if not make newBase-->base10
			int finalBase = newBase;
			if(this.base > newBase) {
			newBase = 10;
			result.base=10;
		}
		
		MyBigInteger oldBase = new MyBigInteger(this.base,newBase);	
		MyBigInteger original = this.clone();
		original.base = newBase;
		
		MyBigInteger temp = new MyBigInteger(original.coefficients.get(original.coefficients.size()-1),newBase);
		temp = temp.times(oldBase);
		result = result.plus(temp);

			for(int i = original.coefficients.size()-2; i>0; i--) {
				temp = new MyBigInteger(original.coefficients.get(i),newBase);
				result = result.plus(temp);
				result= result.times(oldBase);	
			}
			temp = new MyBigInteger(original.coefficients.get(0),newBase);
			result = result.plus(temp);
		
		
		//base10 (stored in result) to newBase
		if(this.base>finalBase) {

			MyBigInteger finalBaseBig = new MyBigInteger(finalBase,10);
			MyBigInteger zero = new MyBigInteger(0,10);
			MyBigInteger o = new MyBigInteger(10);

			while (result.compareTo(zero) == 1) {
				o.coefficients.add(result.mod(finalBaseBig).coefficients.get(0));
				result = result.dividedBy(finalBaseBig);
			}
			
			result = o.clone();
			result.base = finalBase;
		}
		
		
		return result;
	}
	
	public ArrayList<MyBigInteger>  primeFactors() throws Exception {
		
		ArrayList<MyBigInteger> factors = new ArrayList<MyBigInteger>();

		MyBigInteger two = new MyBigInteger(2,this.base);
		MyBigInteger zero = new MyBigInteger(0,this.base);
		MyBigInteger copy = this.clone();

		//first prime number = 2
		//We started at 2 so we can avoid even numbers and do int += 2
		while((copy.mod(two)).compareTo(zero) == 0) {
			factors.add(two);
			copy = copy.dividedBy(two);
		}
		
		MyBigInteger number = new MyBigInteger(3,this.base);
		
		//iterate though the prime numbers (all the numbers actually but already done if multiple of prime)
		int i = 3;
		while(number.times(number).compareTo(copy) <= 0) { 
			while(copy.mod(number).compareTo(zero) == 0) {
				factors.add(number);
				copy = copy.dividedBy(number);
			}
			i+=2;
			number = new MyBigInteger(i,this.base);
		}
		if(copy.compareTo(two) == 1) {
			factors.add(copy);
		}
		
		if(factors.size()==0) {
			factors.add(this);
		}
		return factors;

	}
		



	//   ----------------   HELPER METHODS --------------------------
	
	/*  
	 *  The mod method divides 'this' by 'divisor' and returns the remainder.
	*/
	 
	public MyBigInteger mod( MyBigInteger  divisor ) throws Exception{
		return this.minus(this.dividedBy(divisor).times(divisor));		
	}		

	@Override
	public MyBigInteger  clone(){

		//  For technical reasons that don't interest us here, this method 
		//  has to be declared public (not private).

		MyBigInteger copy = new MyBigInteger(this.base);
		for (int i=0; i < this.coefficients.size(); i++){
			copy.coefficients.add( Integer.valueOf( this.coefficients.get(i) ) );
		}
		return copy;
	}
	
	/*
	 *  The a.compareTo(b) method returns -1 if a < b,  it returns 0 if a == b,  
	 *  and it returns 1 if a > b.
	 *  
 	 *  It assumes that the two numbers have the same base.  (One could add code to check this but I didn't.)
	 */
	
	public int compareTo(MyBigInteger second){

		//   if  this < second,  return -1  
		//   if  this > second,  return  1  
		//   otherwise they are equal and return 0
		
		//   Assume maximum degree coefficient is non-zero.   Then,  if two numbers
		//   have different maximum degree, it is easy to decide which is larger.
		
		int diff = this.coefficients.size() - second.coefficients.size();
		if (diff < 0)
			return -1;
		else if (diff > 0)
			return 1;
		else { 
			
			/*   
			 * If two numbers have the same maximum degree,  then it is a bit trickier
			 * to decide which number is larger.   You need to compare the coefficients,
			 * starting from the largest and working toward the smallest until you find
			 * coefficients that are not equal.
			 */
			
			boolean done = false;
			int i = this.coefficients.size() - 1;
			while (i >=0 && !done){
				diff = this.coefficients.get(i) - second.coefficients.get(i); 
				if (diff < 0){
					return -1;
				}
				else if (diff > 0)
					return 1;
				else{ 
					i--;
				}
			}
			return 0;    //   if all coefficients are the same,  so numbers are equal.
		}
	}

	/*  
	 *    computes  'this' * base^n  
	 *    Note that it modifies the number, so if you don't want that, then clone the number first.
	 */
	
	private MyBigInteger timesBaseToThePower(int n){
		MyBigInteger thisShifted = new MyBigInteger(base);
		//  First make a list of n 0's.
		for (int i=0; i< n; i++){
			thisShifted.coefficients.add(Integer.valueOf(0));
		}
		
		//  Then add all the coefficents of 'this' after the n 0's.
		for (int i=0; i< this.coefficients.size(); i++){
			thisShifted.coefficients.add(this.coefficients.get(i));
		}
		return thisShifted;
	}

	/*
	 * 	Returns a string with coefficients in the reverse order which is the natural format for people to reading numbers,
	 *  i.e. people want to read  a[N-1], ... a[2] a[1] a[0]. 
	 */
	
	@Override
	public String toString(){	
		String s = new String(); 
		for (Integer coef : coefficients)     //  Java enhanced for loop
			s = coef.toString() + s ;        //   Append each successive coefficient.
		return "(" + s + ")_" + base;		
	}


	public static ArrayList<MyBigInteger> primesToN(int n, int base) throws Exception {
		
		//  Sieve of Eratosthenes algorithm
		// 
		//  Code here is modified from
		//  https://introcs.cs.princeton.edu/java/14array/PrimeSieve.java.html
		
		// initially assume all integers are prime
		
		boolean[]  isPrime;
		ArrayList<MyBigInteger>  listOfPrimes = new ArrayList<MyBigInteger>();
		
        isPrime = new boolean[n+1];
        for (int i = 2; i <= n; i++) {
            isPrime[i] = true;
        }

        // mark non-primes <= n using Sieve of Eratosthenes
        for (int factor = 2; factor*factor <= n; factor++) {

            // If factor is prime, then mark multiples of factor as nonprime
            // It suffices to consider multiples factor, factor+1, ...,  n/factor.
            if (isPrime[factor]) {
                for (int j = factor; factor*j <= n; j++) {
                    isPrime[factor*j] = false;
                }
            }
        }
		for (int i = 2;  i <= n;  i++) {
			if (isPrime[i] == true)
				listOfPrimes.add( new MyBigInteger(i,base));
		}
		return listOfPrimes;
	}
}