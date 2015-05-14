/**	
 *	Name: Nicholas Low Jun Han
 *	Matric No.: A0110574N
 *	PA1 - D/E
 */

import java.util.*;
import java.lang.*;
import java.io.*;

class KatsuSoba {

	public static void main(String[] args) {
		Scanner sc = new Scanner(System.in);
        PrintWriter pw = new PrintWriter(new BufferedWriter(new OutputStreamWriter(System.out))); 
        
        int T, B;
		String V, M, A; 
        T = sc.nextInt();
		
        for (int i = 1; i <= T; ++i) {
            B = sc.nextInt();
			sc.nextLine();
			V = sc.nextLine(); M = sc.nextLine();
			if(V.equals("0") || M.equals("0")) { //if any operand is 0, return 0;
				A = "0";
			} else { //else, employ Karatsuba
				int[] velocity = stringToArray(V);
				//System.out.println("The Velocity array is: " + arrayToString(velocity));
				int[] mass = stringToArray(M);
				//System.out.println("The Mass array is: " + arrayToString(mass));
				int[] result = KatsuSoba(velocity, mass);
				A = arrayToString(result);
			}
			//solution here
			
			pw.write(trimZeros(A));
			pw.write("\n");
        }
        pw.close();
    }

    /*-------------------------KARATSUBA ALGO-------------------------*/
    private static int[] KatsuSoba(int[] a, int[] b) {
    	//System.out.println("Current a: " + arrayToString(a));
    	//System.out.println("Current b: " + arrayToString(b));

    	//remove leading zeroes:
    	a = removeZeroes(a);
    	b = removeZeroes(b);

    	//base case:
    	if(a.length <= 50 && b.length <= 50) {
    		return multiplication(a, b);
    	}

    	//finding max length:
    	if(a.length > b.length) {
    		b = padding(b, a.length - b.length);
    	} else {
    		a = padding(a, b.length - a.length);
    	}

    	//splitting by halves:
    	int R = Math.max(a.length, b.length)/2;

    	int[] Vh = Arrays.copyOfRange(a, 0, a.length - R);
    	int[] Vl = Arrays.copyOfRange(a, a.length - R, a.length);
    	int[] Mh = Arrays.copyOfRange(b, 0, b.length - R);
    	int[] Ml = Arrays.copyOfRange(b, b.length - R, b.length);
    	int[] Msum = arrayAddition(Mh,Ml);
    	int[] Vsum = arrayAddition(Vh,Vl);

    	//recursive calls
    	int[] z0 = KatsuSoba(Vl, Ml);
    	int[] z2 = KatsuSoba(Vh, Mh);
    	int[] z1 = KatsuSoba(Vsum, Msum);

    	//System.out.println("z0: " + arrayToString(z0));
    	//System.out.println("z2: " + arrayToString(z2));
    	//System.out.println("z1: " + arrayToString(z1));

    	//adding and subtracting:
    	return arrayAddition(
    			arrayAddition(
    				baseMultiplication(z2, R*2), 
    					baseMultiplication(
    						arraySubtraction(z1, 
    							arrayAddition(z2,z0)), R)), z0);
    }

    /*-----------------------AUXILLARY FUNCTIONS-----------------------*/
    
    //multiplication of 2 int[]s
    private static int[] multiplication(int[] a, int[] b) {
    	a = removeZeroes(a);
    	b = removeZeroes(b);
		int sizeOfIntermediate = a.length + b.length;
		int positionCounter = sizeOfIntermediate - 1;
		int secondPositionCounter = sizeOfIntermediate -1;
		int[] intermediate = new int[sizeOfIntermediate];
		for(int firstCount = a.length - 1; firstCount >= 0; firstCount--) {
			for(int secondCount = b.length - 1; secondCount >= 0; secondCount--) {
				intermediate[positionCounter] += a[firstCount] * b[secondCount];
				if(intermediate[positionCounter] > 9) {
					intermediate[positionCounter - 1] += intermediate[positionCounter] / 10;
					intermediate[positionCounter] = intermediate[positionCounter] % 10;
				}  
				positionCounter--;
			}
			secondPositionCounter--;
			positionCounter = secondPositionCounter;
		}
		return intermediate;
    }

    //base multiplication
    private static int[] baseMultiplication(int[] a, int b) {
    	a = removeZeroes(a);
    	int length = a.length + b;
    	int[] temp = new int[length];
    	for(int count = a.length - 1; count >= 0; count--) {
    		temp[count] = a[count];
    	}
    	return temp; 
    }

    //addition of 2 int[]s : Both must be equal in length!
    private static int[] arrayAddition(int[] a, int[] b) {
    	a = removeZeroes(a);
    	b = removeZeroes(b);
    	if(a.length > b.length) {
    		b = padding(b, a.length - b.length);
    	} else {
    		a = padding(a, b.length - a.length);
    	}
    	int newLength = a.length + 1;
    	int[] sumArray = new int[newLength];
    	for(int i = a.length - 1; i >= 0; i--) {
    		sumArray[i+1] += a[i] + b[i];
    		if(sumArray[i+1] > 9) {
    			sumArray[i] += 1;
    			sumArray[i+1] = sumArray[i+1] - 10;
    		}
    	}
    	return sumArray;
    }

    //subtraction of 2 int[]s : Both must be equal in length! a must be > b
    private static int[] arraySubtraction(int[] a, int[] b) {
    	a = removeZeroes(a);
    	b = removeZeroes(b);
    	if(a.length > b.length) {
    		b = padding(b, a.length - b.length);
    	} else {
    		a = padding(a, b.length - a.length);
    	}
    	int[] result = new int[a.length];
    	for(int i = a.length - 1; i >= 0; i--) {
			if(a[i] < b[i]) {
				a[i-1] = a[i-1] - 1;
				a[i] += 10;
			}
			result[i] = a[i] - b[i];
		}
		return result;
    }

    //padding of int[] with leading 0's
    public static int[] padding(int[] a, int diff) {
    	int length = a.length;
    	int newlength = length + diff;
    	int[] b = new int[length+diff];
    	for(int c = a.length - 1; c >= 0; c--) {
    		b[c + diff] = a[c];
    	}
    	return b;
    }

    //removal of leading zeroes:
    public static int[] removeZeroes(int[] a) {
    	//System.out.println(arrayToString(a));
    	if(a.length == 1) {
    		return a;
    	}
    	int b = 0;
    	while(a[b] == 0) {
    		if(b == a.length - 1) {
    			break;
    		}	
    		b++;	
    	}
    	return Arrays.copyOfRange(a, b, a.length);
    }

    /*-------------------Input Handling Functions-------------------*/
    private static String arrayToString(int[] a) {
		int length = a.length;
		String stringArray = "";
		for(int i = 0; i < length; i++) {
			stringArray = stringArray + toDigit(a[i]);
		}
		return stringArray;
	}

	private static int[] stringToArray(String a) {
		int sizeOfArray = a.length();
		int[] array = new int[sizeOfArray];
		for(int i = 0; i < a.length(); i++) {
			array[i] = parseDigit(a.charAt(i));
		}
		return array;
	}

	private static char toDigit(int digit) {
		if (digit <= 9) {
			return (char)(digit + '0');
		} 
		return (char)(digit - 10 + 'A');
	}

	private static String trimZeros(String input) {
		int left = 0;
		int right = input.length()-1;
		int fp = input.indexOf('.');
		if (fp == -1) {
			fp = input.length();
		}
		
		while(left < fp-1) {
			if (input.charAt(left) != '0')
				break;
			left++;
		}
		
		while (right >= fp) {
			if (input.charAt(right) != '0') {
				if (input.charAt(right) == '.')
					right--;
				break;
			}
			right--;
		}
		
		if (left >= fp)
			return "0" + input.substring(left,right+1);
		return input.substring(left,right+1);
	}

	private static int parseDigit(char c) {
		if (c <= '9') {
			return c - '0';
		} 
		return c - 'A' + 10;
	}
} 
