import com.mobile.etl.util.LogParserUtil;

import java.util.Map;

/**
 * @ClassName LogUtilTest
 * @Author lyd
 * @Date $ $
 * @Vesion 1.0
 * @Description //TODO $
 **/
public class LogUtilTest {
    public static void main(String[] args) {
        Map<String,String> map = LogParserUtil.parserLog("120.197.87.216^A1496121112.367^Ahh^A/CfImg.gif?en=e_l&ver=1&pl=website&sdk=js&u_ud=D4289356-5BC9-47C4-8F7D-A16022833E7E&u_sd=FB47F1DG-2C1B-4F41-8C38-344040ABCCA0&c_time=1450051932367&l=zh-CN&b_iev=Mozilla%2F5.0%20(Windows%20NT%206.1%3B%20WOW64)%20AppleWebKit%2F537.36%20(KHTML%2C%20like%20Gecko)%20Chrome%2F46.0.2490.71%20Safari%2F537.36&b_rst=1280*768");

        for (Map.Entry<String,String> en:map.entrySet()){
            System.out.println(en.getKey()+":"+en.getValue());
        }
    }
}