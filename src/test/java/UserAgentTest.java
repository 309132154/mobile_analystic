import com.mobile.etl.util.UserAgentUtil;

/**
 * @ClassName UserAgentTest
 * @Author lyd
 * @Date $ $
 * @Vesion 1.0
 * @Description //TODO $
 **/
public class UserAgentTest {

    public static void main(String[] args) {
        System.out.println(UserAgentUtil.parserUserAgent("Mozilla/5.0 (Windows NT 6.1; WOW64) AppleWebKit/537.36 (KHTML, like Gecko) Chrome/58.0.3029.110 Safari/537.36 SE 2.X MetaSr 1.0"));
        System.out.println(UserAgentUtil.parserUserAgent("Mozilla/5.0 (Windows NT 6.1; WOW64) AppleWebKit/537.36 (KHTML, like Gecko) Chrome/31.0.1650.63 Safari/537.36"));
    }
}