package com.boil.gobang;

import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.JSONObject;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;
import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.PrintWriter;
import java.util.HashMap;
import java.util.Map;

public class GoBangServlet extends HttpServlet {

    private Map<String, Snapshot> snapshot = new HashMap<String, Snapshot>();

    @Override
    protected void doPost(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
        HttpSession session = request.getSession(true);

        Snapshot ss;
        if (session.isNew()) {
            ss =  new Snapshot();
            String id = session.getId();
            snapshot.put(id, ss);
        } else {
            String id = session.getId();
            ss = snapshot.get(id);
        }

        BufferedReader br = new BufferedReader(new InputStreamReader(request.getInputStream(),"utf-8"));
        String line;
        StringBuilder sb = new StringBuilder();
        while ((line = br.readLine()) != null) {
            sb.append(line);
        }

        JSONObject obj = JSON.parseObject(sb.toString());
        if (obj != null) {
            Integer px = obj.getInteger("x");
            Integer py = obj.getInteger("y");
            ss.getcolor()[px][py] = Color.White;
        }

        // 设置响应内容类型
        response.setContentType("application/json;charset=UTF-8");
        PrintWriter out = response.getWriter();

        Color[][] color = ss.getcolor();
        String json = "[";
        for (int x = 0; x < 15; x++) {
            Color[] row = color[x];
            json += "[";
            for (int y = 0; y < 15; y++) {
                Color c = row[y];
                json += (c == Color.Nul ? 0 : c == Color.White ? 1 : 2) + ",";
            }
            json = json.substring(0, json.length() - 1);
            json += "],";
        }
        json = json.substring(0, json.length() - 1);
        json += "]";
        out.write(json);
    }

}
