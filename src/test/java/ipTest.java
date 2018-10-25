import com.mobile.etl.util.IPParserUtil;
import com.mobile.etl.util.ip.IPSeeker;

import java.util.List;

/**
 * @ClassName ipTest
 * @Author lyd
 * @Date $ $
 * @Vesion 1.0
 * @Description //TODO $
 **/
public class ipTest extends IPSeeker {

    public static void main(String[] args) throws Exception{

//        System.out.println(IPSeeker.getInstance().getCountry("62.76.155.255"));
//        System.out.println(":"+IPParserUtil.parserIp("62.76.155.255"));
        System.out.println(new IPParserUtil().parserIp1("http://ip.taobao.com/service/getIpInfo.php?ip="+"62.76.155.255","utf-8"));
        /* List<String> ips = IPSeeker.getInstance().getAllIp();
        for (String ip:ips){
            System.out.println(ip);
            System.out.println(ip+":"+IPParserUtil.parserIp(ip));
        }*/

    }
}