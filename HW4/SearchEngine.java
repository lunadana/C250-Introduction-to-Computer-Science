import java.util.HashMap;
import java.util.ArrayList;

public class SearchEngine {
	public HashMap<String, ArrayList<String> > wordIndex;   // this will contain a set of pairs (String, LinkedList of Strings)	
	public MyWebGraph internet;
	public XmlParser parser;

	public SearchEngine(String filename) throws Exception{
		this.wordIndex = new HashMap<String, ArrayList<String>>();
		this.internet = new MyWebGraph();
		this.parser = new XmlParser(filename);
	}
	
	/* 
	 * This does a graph traversal of the web, starting at the given url.
	 * For each new page seen, it updates the wordIndex, the web graph,
	 * and the set of visited vertices.
	 
	 * 	This method will fit in about 30-50 lines (or less)
	 */
	
	public void crawlAndIndex(String url) throws Exception {
		// TODO : Add code here
		
		ArrayList<String> temp = parser.getContent(url);
		ArrayList<String> temp1 = parser.getContent(url);
		
		for(int i = 0; i<temp.size();i++) {
			if(temp.contains(temp.get(i))) {
				continue;
			}
			temp1.add(temp.get(i).toLowerCase());	
		}
		
		this.wordIndex.put(url,temp);

		ArrayList<String> hyperlinks = parser.getLinks(url);
		
		internet.addVertex(url);
		internet.setVisited(url, true);

		for(int i = 0; i<hyperlinks.size();i++) {
			internet.addVertex(hyperlinks.get(i));
			internet.addEdge(url, hyperlinks.get(i));

			if(!internet.getVisited(hyperlinks.get(i))) {
				crawlAndIndex(hyperlinks.get(i));
			}
		}
		
	}
	
	/* 
	 * This computes the pageRanks for every vertex in the web graph.
	 * It will only be called after the graph has been constructed using
	 * crawlAndIndex(). 
	 * To implement this method, refer to the algorithm described in the 
	 * assignment pdf. 
	 * 
	 * This method will probably fit in about 30 lines.
	 */
	public void assignPageRanks(double epsilon) {
		// TODO : Add code here
		
		ArrayList<String> vertices = new ArrayList<String>();
		vertices = internet.getVertices();

		for(int i = 0; i<vertices.size();i++) {
			internet.setPageRank(vertices.get(i), 1.0);
		}
		
		ArrayList<Double> a1 = new ArrayList<Double>();
		ArrayList<Double> a2 = new ArrayList<Double>();
		
		a1=this.computeRanks(vertices);
		double d1 = a1.get(0);
		a2 = this.computeRanks(vertices);
		double d2 = a2.get(0);

		while(!(Math.abs(d1-d2)<epsilon)) {
			d1=d2;
			a2=this.computeRanks(vertices);
			d2=a2.get(0);
		}
		
		}
	

	/*
	 * The method takes as input an ArrayList<String> representing the urls in the web graph 
	 * and returns an ArrayList<double> representing the newly computed ranks for those urls. 
	 * Note that the double in the output list is matched to the url in the input list using 
	 * their position in the list.
	 */
	public ArrayList<Double> computeRanks(ArrayList<String> vertices) {
		// TODO : Add code here
		
		ArrayList<Double> verticleRank = new ArrayList<Double>();
		ArrayList<String> temp = new ArrayList<String>();
		
		for(int i = 0; i<vertices.size();i++) {
			
		temp = internet.getEdgesInto(vertices.get(i));
		Double addition = 0.0;
		
			for(int j = 0; j<temp.size();j++) {
				addition += internet.getPageRank(temp.get(j))/internet.getOutDegree(temp.get(j));
			}
			
			internet.setPageRank(vertices.get(i),0.5+0.5*addition);
			verticleRank.add(i, 0.5+0.5*addition);
		}
		
		return verticleRank;
	}

	
	/* Returns a list of urls containing the query, ordered by rank
	 * Returns an empty list if no web site contains the query.
	 * 
	 * This method should take about 25 lines of code.
	 */
	
	public ArrayList<String> getResults(String query) {
		// TODO: Add code here
		HashMap<String, Double> temp = new HashMap<String, Double>();

		ArrayList<String> vertices = new ArrayList<String>();
		vertices = internet.getVertices();
		
		ArrayList<String> words = new ArrayList<String>();

		for(int i = 0; i<vertices.size(); i++) {
			words = wordIndex.get(vertices.get(i));
			if(words.contains(query)) {
				temp.put(vertices.get(i),internet.getPageRank(vertices.get(i)));
			}
		}
		
		ArrayList<String> result = new ArrayList<String>();
		result = Sorting.fastSort(temp);
		
		return result;
		
		}
}
