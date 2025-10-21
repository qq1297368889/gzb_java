package start;

import com.tfb01.dao.WorldDao;
import com.tfb01.dao.impl.WorldDaoImpl;
import com.tfb01.entity.World;
import gzb.tools.Tools;

public class Test {
    public static void main(String[] args) throws Exception {
        WorldDao worldDao=new WorldDaoImpl();
        System.out.println(worldDao.query(new World(),null,null,1,100,-1));

    }
}
