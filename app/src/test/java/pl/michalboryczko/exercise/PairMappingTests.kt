package pl.michalboryczko.exercise

import org.junit.Test
import junit.framework.Assert.assertEquals
import pl.michalboryczko.exercise.source.CryptocurrencyMapper

class PairMappingTests: BaseTest() {

    private val mapper = CryptocurrencyMapper()




    @Test
    fun mergeCryptocurrencyPairAndTickerTest(){
        val cryptocurrencyPair = mapper.mergeCryptocurrencyPairAndTicker(pairResponseMock, tickerResponseMock)
        assertEquals(cryptocurrencyPair.id, "123")
        assertEquals(cryptocurrencyPair.pair, "BTC/ETH")
        assertEquals(cryptocurrencyPair.baseCurrency, "BTC")
        assertEquals(cryptocurrencyPair.quoteCurrency, "ETH")
    }

    @Test
    fun createCryptocurrencyUrlTest(){
        val url = mapper.createCryptocurrencyUrl("BTC")
        assertEquals(btcUrl, url)
    }

    @Test
    fun createBaseCryptocurrencyUrlFromPairTest(){
        val cryptocurrencyPair = mapper.mergeCryptocurrencyPairAndTicker(pairResponseMock, tickerResponseMock)
        val url = mapper.createBaseCryptocurrencyUrlFromPair(cryptocurrencyPair.pair)
        assertEquals(btcUrl, url)
    }

    @Test
    fun createQuoteCryptocurrencyUrlFromPairTest(){
        val cryptocurrencyPair = mapper.mergeCryptocurrencyPairAndTicker(pairResponseMock, tickerResponseMock)
        val url = mapper.createQuoteCryptocurrencyUrlFromPair(cryptocurrencyPair.pair)
        assertEquals(ethUrl, url)
    }

    @Test
    fun createPairStringTest(){
        val pairString = mapper.createPairString(simpleBtcEthPair)
        assertEquals("btceth", pairString)
    }

    @Test
    fun createPairStringSlashSeparatedTest(){
        val pairString = mapper.createPairStringSlashSeparated(simpleBtcEthPair)
        assertEquals("BTC/ETH", pairString)
    }

}