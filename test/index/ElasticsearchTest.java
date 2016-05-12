package index;

import java.io.File;
import java.io.IOException;

import org.elasticsearch.action.search.SearchResponse;
import org.elasticsearch.action.search.SearchType;
import org.elasticsearch.client.Client;
import org.elasticsearch.index.query.QueryBuilders;
import org.junit.AfterClass;
import org.junit.BeforeClass;

import com.typesafe.config.Config;
import com.typesafe.config.ConfigFactory;

import controllers.Index;

@SuppressWarnings("javadoc")
public abstract class ElasticsearchTest {

	public static final Config CONFIG =
			ConfigFactory.parseFile(new File("conf/application.conf")).resolve();

	protected static Client client = Index.CLIENT;

	@BeforeClass
	public static void makeIndex() throws IOException {
		Index.initializeIndex("test/transformation/enriched-test.json");
	}

	@AfterClass
	public static void closeElasticSearch() {
		client.close();
	}

	public static SearchResponse exactSearch(final String aField,
			final String aValue) {
		final SearchResponse responseOfSearch =
				client.prepareSearch(CONFIG.getString("index.es.name"))
						.setTypes(CONFIG.getString("index.es.type"))
						.setSearchType(SearchType.DFS_QUERY_AND_FETCH)
						.setQuery(QueryBuilders.termQuery(aField, aValue)).execute()
						.actionGet();
		return responseOfSearch;
	}

	public static SearchResponse search(final String aField,
			final String aValue) {
		SearchResponse responseOfSearch =
				client.prepareSearch(CONFIG.getString("index.es.name"))
						.setTypes(CONFIG.getString("index.es.type"))
						.setSearchType(SearchType.DFS_QUERY_AND_FETCH)
						.setQuery(QueryBuilders.matchQuery(aField, aValue)).execute()
						.actionGet();
		return responseOfSearch;
	}

}
