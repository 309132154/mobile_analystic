import com.mobile.analystic.modle.base.BrowserDimension;
import com.mobile.analystic.modle.base.PlatformDimension;
import com.mobile.analystic.mr.service.DimensionToMysqlI;
import com.mobile.analystic.mr.service.impl.DimensionToMysqlImpl;

/**
 * @ClassName DimensionTest
 * @Author lyd
 * @Date $ $
 * @Vesion 1.0
 * @Description //TODO $
 **/
public class DimensionTest {
    public static void main(String[] args) {
        DimensionToMysqlI i = new DimensionToMysqlImpl();
//        PlatformDimension pl = new PlatformDimension("website11");
        BrowserDimension br = new BrowserDimension("IE","11");
        System.out.println(i.getDimensionIdByDimension(br));
    }
}