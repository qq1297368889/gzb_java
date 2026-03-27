package com.tfb01.api;

import com.tfb01.dao.FortuneDao;
import com.tfb01.entity.Fortune;
import com.tfb01.entity.Message;
import com.tfb01.entity.World;
import com.tfb01.dao.WorldDao;
import gzb.frame.annotation.*;
import gzb.frame.netty.entity.Request;
import gzb.frame.netty.entity.Response;
import gzb.tools.Tools;
import gzb.tools.cache.object.ObjectCache;

import java.util.Comparator;
import java.util.concurrent.ThreadLocalRandom;
import java.util.List;
import java.util.concurrent.atomic.AtomicInteger;

@Controller
@RequestMapping(value = "/") // TFB 通常要求根路径或指定路径
@Header(item = {@HeaderItem(key = "Content-Type", val = "application/json;charset=utf-8")})
public class TfbController {
    @Resource(value = "com.tfb01.dao.impl.WorldDaoImpl")
    WorldDao worldDao;

    @PostMapping("updates0")
    @GetMapping("updates0")
    @ManualRespond
    public Object updates0(int queries, Response response) throws Exception {
        if (queries < 1) {
            queries = 1;
        }
        if (queries > 500) {
            queries = 500;
        }
        List<World> worlds = worldDao.query(queries);
        AtomicInteger atomicInteger = new AtomicInteger(0);
        for (World world : worlds) {
            int newNum;
            do {
                newNum = ThreadLocalRandom.current().nextInt(10000) + 1;
            } while (newNum == world.getRandomNumber());
            world.setRandomNumber(newNum);
            worldDao.updateAsync(world, () -> {
                response.sendAndFlush("server error");
            }, () -> {
                if (atomicInteger.incrementAndGet() == worlds.size()) {
                    response.sendAndFlush(worlds);
                }
            });
        }
        return null;
    }

    @Resource
    FortuneDao fortuneDao;


    @GetMapping("fortunes")
    @Header(item = {@HeaderItem(key = "Content-Type", val = "text/html;charset=utf-8")})
    public String fortunes() throws Exception {
        Fortune fortune = new Fortune();
        List<Fortune> fortunes = fortuneDao.query(fortune);
        fortunes.add(fortune.setId(0).setMessage("Additional fortune added at request time."));
        fortunes.sort(Comparator.comparing(Fortune::getMessage));
        ObjectCache.Entity entity0=ObjectCache.SB_CACHE0.get();
        int index=entity0.open();
        try {
            StringBuilder sb = entity0.get(index);
            sb.append("<!DOCTYPE html><html><head><title>Fortunes</title></head><body><table><tr><th>id</th><th>message</th></tr>");
            for (Fortune fortune0 : fortunes) {
                sb.append("<tr><td>").append(fortune0.getId()).append("</td><td>");
                escapeHtml(fortune0.getMessage(),sb);
                sb .append("</td></tr>");
            }
            sb.append("</table></body></html>");
            return sb.toString();
        }finally {
            entity0.close(index);
        }
    }

    private void escapeHtml(String text,StringBuilder result) {
        for (int i = 0; i < text.length(); i++) {
            char c = text.charAt(i);
            switch (c) {
                case '&': result.append("&amp;"); break;
                case '<': result.append("&lt;"); break;
                case '>': result.append("&gt;"); break;
                case '"': result.append("&quot;"); break;
                case '\'': result.append("&#39;"); break;
                default: result.append(c); break;
            }
        }
    }
    //内部收集sql 最终框架统一提交  模拟事务
    @PostMapping("updates")
    @GetMapping("updates")
    @Transaction(simulate = true)
    public Object updates(int queries) throws Exception {
        if (queries < 1) {
            queries = 1;
        }
        if (queries > 500) {
            queries = 500;
        }
        List<World> worlds = worldDao.query(queries);
        for (World world : worlds) {
            int newNum;
            do {
                newNum = ThreadLocalRandom.current().nextInt(10000) + 1;
            } while (newNum == world.getRandomNumber());
            world.setRandomNumber(newNum);
            if (worldDao.update(world) == -1) {
                return "server error";
            }
        }
        return worlds;
    }


    @EventLoop
    @GetMapping("json")
    public Object json() {
        Message message = new Message();
        message.message = "Hello, World!";
        return message;
    }

    @GetMapping("db")
    public Object db() throws Exception {
        int id = ThreadLocalRandom.current().nextInt(10000) + 1;
        World w = new World();
        w.setId(id);
        return worldDao.find(w);
    }

    @GetMapping("queries")
    public Object queries(int queries) throws Exception {
        if (queries < 1) {
            queries = 1;
        }
        if (queries > 500) {
            queries = 500;
        }
        return worldDao.query(queries);
    }


    @GetMapping("db2")
    public Object db2() throws Exception {
        init();
        int id = ThreadLocalRandom.current().nextInt(10000) + 1;
        return data[id];
    }

    World[] data = null;

    private void init() throws Exception {
        if (data == null) {
            data = new World[10001];
            for (World world : worldDao.query(new World())) {
                data[world.getId()] = (world);
            }
        }
    }

    @EventLoop
    @GetMapping("cache")
    public Object cache(int queries) throws Exception {
        if (queries < 1) {
            queries = 1;
        }
        if (queries > 500) {
            queries = 500;
        }
        init();
        World[] worlds = new World[queries];
        for (int i = 0; i < worlds.length; i++) {
            worlds[i] = data[Tools.getRandomInt(1, 10000)];
        }
        return worlds;
    }

    @GetMapping("plaintext")
    @Header(item = {@HeaderItem(key = "Content-Type", val = "text/plain;charset=utf-8")})
    public Object plaintext() {
        return "Hello, World!";
    }


}