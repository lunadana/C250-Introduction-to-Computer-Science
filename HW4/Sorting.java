import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map.Entry; // You may need it to implement fastSort

public class Sorting {

	/*
	 * This method takes as input an HashMap with values that are Comparable. 
	 * It returns an ArrayList containing all the keys from the map, ordered 
	 * in descending order based on the values they mapped to. 
	 * 
	 * The time complexity for this method is O(n^2) as it uses bubble sort, where n is the number 
	 * of pairs in the map. 
	 */
    public static <K, V extends Comparable> ArrayList<K> slowSort (HashMap<K, V> results) {
        ArrayList<K> sortedUrls = new ArrayList<K>();
        sortedUrls.addAll(results.keySet());	//Start with unsorted list of urls

        int N = sortedUrls.size();
        for(int i=0; i<N-1; i++){
			for(int j=0; j<N-i-1; j++){
				if(results.get(sortedUrls.get(j)).compareTo(results.get(sortedUrls.get(j+1))) <0){
					K temp = sortedUrls.get(j);
					sortedUrls.set(j, sortedUrls.get(j+1));
					sortedUrls.set(j+1, temp);					
				}
			}
        }
        return sortedUrls;                    
    }
    
    
	/*
	 * This method takes as input an HashMap with values that are Comparable. 
	 * It returns an ArrayList containing all the keys from the map, ordered 
	 * in descending order based on the values they mapped to. 
	 * 
	 * The time complexity for this method is O(n*log(n)), where n is the number 
	 * of pairs in the map. 
	 */
    public static <K, V extends Comparable> ArrayList<K> fastSort(HashMap<K, V> results) {

    	 ArrayList<K> sortedUrls = new ArrayList<K>();
         sortedUrls.addAll(results.keySet());
    	
         return mergeSort(sortedUrls, results);
    	
    }
    public static <K, V extends Comparable> ArrayList<K>  mergeSort(ArrayList<K> list, HashMap<K, V> results){
    	
    	if(list.size()<10) {
			int k;
			for(int i=1;i<list.size();i++) {
				K temp=list.get(i);
				k=i;
				while(k>0 && (results.get(temp).compareTo(results.get(list.get(0)))) >=0) {
					list.set(k, list.get(k-1));
					k=k-1;
				
				}
				list.set(k,temp);
		}		return list;
    	}
    	else {
    		
    	ArrayList<K> list1 = new ArrayList<K>();
        ArrayList<K> list2 = new ArrayList<K>();
        	
    	int mid = (list.size()-1)/2;
    	for(int i = 0; i<=mid; i++) {
    		list1.add(list.get(i));
    	}
    	for(int i=mid+1; i<list.size(); i++) {
    		list2.add(list.get(i));
    	}
    	
    	list1=mergeSort(list1,results);
    	list2=mergeSort(list2,results);
    	
    	return merge(list1,list2,results);
    	}
    	
  
    }
    
    public static <K, V extends Comparable> ArrayList<K>  merge(ArrayList<K> list1, ArrayList<K> list2, HashMap<K, V> results){
    	
    	ArrayList<K> list = new ArrayList<K>();
    	
    	while(!list1.isEmpty() && !list2.isEmpty()) {
    		if(results.get(list1.get(0)).compareTo(results.get(list2.get(0))) < 0){
    			list.add(list2.remove(0));
    		}else {
    			list.add(list1.remove(0));
    		}
    	}
    	while(!list1.isEmpty()) {
    		list.add(list1.remove(0));
    	}
    	while(!list2.isEmpty()) {
    		list.add(list2.remove(0));
    	}
    	
    	return list;
    }
    
    
    

}