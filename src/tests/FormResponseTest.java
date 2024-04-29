package tests;

import Core.*;
import org.junit.Test;
import static org.junit.Assert.assertTrue;

public class FormResponseTest {

    @Test
    public void testGetForm(){
        RequestParser rp = new ClientRequestParser(null);
        rp.setRequest("GET /form?foo=1&bar=2 HTTP/1.1");
        ResponseNode node = new FormResponse().generateGETForm(rp);
        assertTrue(ResponseNode.convertBytesToString(node.getContent())
                .contains("<li>foo: 1</li>"));
        assertTrue(ResponseNode.convertBytesToString(node.getContent())
                .contains("<li>bar: 2</li>"));
    }
}
