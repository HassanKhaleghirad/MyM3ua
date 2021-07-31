package com.example.hassan.demo.transport;


import com.example.hassan.demo.transport.M3UALink;
import com.example.hassan.demo.transport.M3UALinkConfiguration;
import com.example.hassan.demo.transport.M3UALinkManager;
import java.util.LinkedHashMap;
import java.util.Objects;

public class M3UALinkManagerImp implements M3UALinkManager {
    private LinkedHashMap<Long, M3UALink> links = new LinkedHashMap();

    public M3UALinkManagerImp() {
    }

    public M3UALink getLink(M3UALinkConfiguration configuration) {
        long id = generateLinkId(configuration);
        M3UALink link = this.getLink(id);
        if (link != null) {
            return link;
        } else {
            link = new M3UALink(id, configuration);
            this.links.put(id, link);
            return link;
        }
    }

    public M3UALink getLink(long linkId) {
        return (M3UALink)this.links.get(linkId);
    }

    static long generateLinkId(M3UALinkConfiguration configuration) {
        return generateId(configuration.getClientAddress(), configuration.getClientPort(), configuration.getServerAddress(), configuration.getServerPort());
    }

    static long generateId(String clientIp, String clientPort, String serverIp, String serverPort) {
        return (long)Objects.hash(new Object[]{clientIp, clientPort, serverIp, serverPort});
    }
}
