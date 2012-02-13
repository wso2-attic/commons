#/bin/sh

echo "Warm-up..."
./loop.sh 2 requests/1K_buyStocks.xml 1000 20   urn:buyStocks.2 http://testb.wso2.com:8280/soap/DirectProxy
./loop.sh 2 requests/1K_buyStocks.xml 1000 20   urn:buyStocks.2 http://testb.wso2.com:8280/soap/CBRProxy
./loop.sh 2 requests/1K_buyCustomStocks.xml 1000 20   urn:buyStocks.2 http://testb.wso2.com:8280/soap/XSLTProxy

echo "Begin performance test..."
echo `date`

# ./loop.sh <iterations> <request.file> <n> <c> <action> <url>

#Direct Proxy
#500 bytes
echo "Direct 500B"
./loop.sh 1 requests/500B_buyStocks.xml 1000 20   urn:buyStocks.2 http://testb.wso2.com:8280/soap/DirectProxy
./loop.sh 1 requests/500B_buyStocks.xml 1000 40   urn:buyStocks.2 http://testb.wso2.com:8280/soap/DirectProxy
./loop.sh 1 requests/500B_buyStocks.xml 100  80   urn:buyStocks.2 http://testb.wso2.com:8280/soap/DirectProxy
./loop.sh 1 requests/500B_buyStocks.xml 100  160  urn:buyStocks.2 http://testb.wso2.com:8280/soap/DirectProxy
./loop.sh 1 requests/500B_buyStocks.xml 100  320  urn:buyStocks.2 http://testb.wso2.com:8280/soap/DirectProxy
./loop.sh 1 requests/500B_buyStocks.xml 10   640  urn:buyStocks.2 http://testb.wso2.com:8280/soap/DirectProxy
./loop.sh 1 requests/500B_buyStocks.xml 10   1280 urn:buyStocks.2 http://testb.wso2.com:8280/soap/DirectProxy
./loop.sh 1 requests/500B_buyStocks.xml 10   2560 urn:buyStocks.2 http://testb.wso2.com:8280/soap/DirectProxy

# 1K
echo "Direct 1K"
./loop.sh 1 requests/1K_buyStocks.xml 1000 20   urn:buyStocks.2 http://testb.wso2.com:8280/soap/DirectProxy
./loop.sh 1 requests/1K_buyStocks.xml 1000 40   urn:buyStocks.2 http://testb.wso2.com:8280/soap/DirectProxy
./loop.sh 1 requests/1K_buyStocks.xml 100  80   urn:buyStocks.2 http://testb.wso2.com:8280/soap/DirectProxy
./loop.sh 1 requests/1K_buyStocks.xml 100  160  urn:buyStocks.2 http://testb.wso2.com:8280/soap/DirectProxy
./loop.sh 1 requests/1K_buyStocks.xml 100  320  urn:buyStocks.2 http://testb.wso2.com:8280/soap/DirectProxy
./loop.sh 1 requests/1K_buyStocks.xml 10   640  urn:buyStocks.2 http://testb.wso2.com:8280/soap/DirectProxy
./loop.sh 1 requests/1K_buyStocks.xml 10   1280 urn:buyStocks.2 http://testb.wso2.com:8280/soap/DirectProxy
./loop.sh 1 requests/1K_buyStocks.xml 10   2560 urn:buyStocks.2 http://testb.wso2.com:8280/soap/DirectProxy

# 5K
echo "Direct 5K"
./loop.sh 1 requests/5K_buyStocks.xml 200 20   urn:buyStocks.6 http://testb.wso2.com:8280/soap/DirectProxy
./loop.sh 1 requests/5K_buyStocks.xml 200 40   urn:buyStocks.6 http://testb.wso2.com:8280/soap/DirectProxy
./loop.sh 1 requests/5K_buyStocks.xml 20  80   urn:buyStocks.6 http://testb.wso2.com:8280/soap/DirectProxy
./loop.sh 1 requests/5K_buyStocks.xml 20  160  urn:buyStocks.6 http://testb.wso2.com:8280/soap/DirectProxy
./loop.sh 1 requests/5K_buyStocks.xml 20  320  urn:buyStocks.6 http://testb.wso2.com:8280/soap/DirectProxy
./loop.sh 1 requests/5K_buyStocks.xml 2   640  urn:buyStocks.6 http://testb.wso2.com:8280/soap/DirectProxy
./loop.sh 1 requests/5K_buyStocks.xml 2   1280 urn:buyStocks.6 http://testb.wso2.com:8280/soap/DirectProxy
./loop.sh 1 requests/5K_buyStocks.xml 2   2560 urn:buyStocks.6 http://testb.wso2.com:8280/soap/DirectProxy

#CBR Proxy
#500 bytes
echo "Direct 500B"
./loop.sh 1 requests/500B_buyStocks.xml 1000 20   urn:buyStocks.2 http://testb.wso2.com:8280/soap/CBRProxy
./loop.sh 1 requests/500B_buyStocks.xml 1000 40   urn:buyStocks.2 http://testb.wso2.com:8280/soap/CBRProxy
./loop.sh 1 requests/500B_buyStocks.xml 100  80   urn:buyStocks.2 http://testb.wso2.com:8280/soap/CBRProxy
./loop.sh 1 requests/500B_buyStocks.xml 100  160  urn:buyStocks.2 http://testb.wso2.com:8280/soap/CBRProxy
./loop.sh 1 requests/500B_buyStocks.xml 100  320  urn:buyStocks.2 http://testb.wso2.com:8280/soap/CBRProxy
./loop.sh 1 requests/500B_buyStocks.xml 10   640  urn:buyStocks.2 http://testb.wso2.com:8280/soap/CBRProxy
./loop.sh 1 requests/500B_buyStocks.xml 10   1280 urn:buyStocks.2 http://testb.wso2.com:8280/soap/CBRProxy
./loop.sh 1 requests/500B_buyStocks.xml 10   2560 urn:buyStocks.2 http://testb.wso2.com:8280/soap/CBRProxy

# 1K
echo "CBR 1K"
./loop.sh 1 requests/1K_buyStocks.xml 1000 20   urn:buyStocks.2 http://testb.wso2.com:8280/soap/CBRProxy
./loop.sh 1 requests/1K_buyStocks.xml 1000 40   urn:buyStocks.2 http://testb.wso2.com:8280/soap/CBRProxy
./loop.sh 1 requests/1K_buyStocks.xml 100  80   urn:buyStocks.2 http://testb.wso2.com:8280/soap/CBRProxy
./loop.sh 1 requests/1K_buyStocks.xml 100  160  urn:buyStocks.2 http://testb.wso2.com:8280/soap/CBRProxy
./loop.sh 1 requests/1K_buyStocks.xml 100  320  urn:buyStocks.2 http://testb.wso2.com:8280/soap/CBRProxy
./loop.sh 1 requests/1K_buyStocks.xml 10   640  urn:buyStocks.2 http://testb.wso2.com:8280/soap/CBRProxy
./loop.sh 1 requests/1K_buyStocks.xml 10   1280 urn:buyStocks.2 http://testb.wso2.com:8280/soap/CBRProxy
./loop.sh 1 requests/1K_buyStocks.xml 10   2560 urn:buyStocks.2 http://testb.wso2.com:8280/soap/CBRProxy

# 5K
echo "CBR 5K"
./loop.sh 1 requests/5K_buyStocks.xml 200 20   urn:buyStocks.6 http://testb.wso2.com:8280/soap/CBRProxy
./loop.sh 1 requests/5K_buyStocks.xml 200 40   urn:buyStocks.6 http://testb.wso2.com:8280/soap/CBRProxy
./loop.sh 1 requests/5K_buyStocks.xml 20  80   urn:buyStocks.6 http://testb.wso2.com:8280/soap/CBRProxy
./loop.sh 1 requests/5K_buyStocks.xml 20  160  urn:buyStocks.6 http://testb.wso2.com:8280/soap/CBRProxy
./loop.sh 1 requests/5K_buyStocks.xml 20  320  urn:buyStocks.6 http://testb.wso2.com:8280/soap/CBRProxy
./loop.sh 1 requests/5K_buyStocks.xml 2   640  urn:buyStocks.6 http://testb.wso2.com:8280/soap/CBRProxy
./loop.sh 1 requests/5K_buyStocks.xml 2   1280 urn:buyStocks.6 http://testb.wso2.com:8280/soap/CBRProxy
./loop.sh 1 requests/5K_buyStocks.xml 2   2560 urn:buyStocks.6 http://testb.wso2.com:8280/soap/CBRProxy

#XSLT Proxy
#500 bytes
echo "Direct 500B"
./loop.sh 1 requests/500B_buyCustomStocks.xml 1000 20   urn:buyStocks.2 http://testb.wso2.com:8280/soap/XSLTProxy
./loop.sh 1 requests/500B_buyCustomStocks.xml 1000 40   urn:buyStocks.2 http://testb.wso2.com:8280/soap/XSLTProxy
./loop.sh 1 requests/500B_buyCustomStocks.xml 100  80   urn:buyStocks.2 http://testb.wso2.com:8280/soap/XSLTProxy
./loop.sh 1 requests/500B_buyCustomStocks.xml 100  160  urn:buyStocks.2 http://testb.wso2.com:8280/soap/XSLTProxy
./loop.sh 1 requests/500B_buyCustomStocks.xml 100  320  urn:buyStocks.2 http://testb.wso2.com:8280/soap/XSLTProxy
./loop.sh 1 requests/500B_buyCustomStocks.xml 10   640  urn:buyStocks.2 http://testb.wso2.com:8280/soap/XSLTProxy
./loop.sh 1 requests/500B_buyCustomStocks.xml 10   1280 urn:buyStocks.2 http://testb.wso2.com:8280/soap/XSLTProxy
./loop.sh 1 requests/500B_buyCustomStocks.xml 10   2560 urn:buyStocks.2 http://testb.wso2.com:8280/soap/XSLTProxy

# 1K
echo "XSLT 1K"
./loop.sh 1 requests/1K_buyCustomStocks.xml 1000 20   urn:buyStocks.2 http://testb.wso2.com:8280/soap/XSLTProxy
./loop.sh 1 requests/1K_buyCustomStocks.xml 1000 40   urn:buyStocks.2 http://testb.wso2.com:8280/soap/XSLTProxy
./loop.sh 1 requests/1K_buyCustomStocks.xml 100  80   urn:buyStocks.2 http://testb.wso2.com:8280/soap/XSLTProxy
./loop.sh 1 requests/1K_buyCustomStocks.xml 100  160  urn:buyStocks.2 http://testb.wso2.com:8280/soap/XSLTProxy
./loop.sh 1 requests/1K_buyCustomStocks.xml 100  320  urn:buyStocks.2 http://testb.wso2.com:8280/soap/XSLTProxy
./loop.sh 1 requests/1K_buyCustomStocks.xml 10   640  urn:buyStocks.2 http://testb.wso2.com:8280/soap/XSLTProxy
./loop.sh 1 requests/1K_buyCustomStocks.xml 10   1280 urn:buyStocks.2 http://testb.wso2.com:8280/soap/XSLTProxy
./loop.sh 1 requests/1K_buyCustomStocks.xml 10   2560 urn:buyStocks.2 http://testb.wso2.com:8280/soap/XSLTProxy

# 5K
echo "XSLT 5K"
./loop.sh 1 requests/5K_buyCustomStocks.xml 200 20   urn:buyStocks.6 http://testb.wso2.com:8280/soap/XSLTProxy
./loop.sh 1 requests/5K_buyCustomStocks.xml 200 40   urn:buyStocks.6 http://testb.wso2.com:8280/soap/XSLTProxy
./loop.sh 1 requests/5K_buyCustomStocks.xml 20  80   urn:buyStocks.6 http://testb.wso2.com:8280/soap/XSLTProxy
./loop.sh 1 requests/5K_buyCustomStocks.xml 20  160  urn:buyStocks.6 http://testb.wso2.com:8280/soap/XSLTProxy
./loop.sh 1 requests/5K_buyCustomStocks.xml 20  320  urn:buyStocks.6 http://testb.wso2.com:8280/soap/XSLTProxy
./loop.sh 1 requests/5K_buyCustomStocks.xml 2   640  urn:buyStocks.6 http://testb.wso2.com:8280/soap/XSLTProxy
./loop.sh 1 requests/5K_buyCustomStocks.xml 2   1280 urn:buyStocks.6 http://testb.wso2.com:8280/soap/XSLTProxy
./loop.sh 1 requests/5K_buyCustomStocks.xml 2   2560 urn:buyStocks.6 http://testb.wso2.com:8280/soap/XSLTProxy

echo "Completed"


