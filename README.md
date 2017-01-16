# Xun Project
This is a tiny Java web framework aiming for those who have little J2EE experience and want to build up a website or web service only using J2SE knowledge. Users can use provided Java annotations to translate any class into a web page or service.

## Features
To use annotations to convert methods into web services, all you need to do is just use @View to mark the request url and use @RenderString to convert the returning value into http response.
```Java
@View(urlPattern = "^test$")
@RenderString
public String foo() {
    return "foo() was invoked!";
}
```

You can use @View with arguments. Use @Param to map request arguments to method arguments (all the arguments will be automatically converted). Besides rendering a string as response, you can use @RenderJson to render a json response.
```Java
@View(urlPattern = "^test$")
@RenderJson
public Map<String, Object> foo(@Param("a") int a, @Param("b") String b) {
    Map<String, Object> result = new HashMap<>();
    result.put("a", a);
    result.put("b", b);

    Map<String, String> inside = new HashMap<>();
    inside.put("lalala", "hahaha");

    result.put("other", inside);
    return result;
}
```

You can use argument fetched from url when you want to write a RESTful web service.
```Java
@View(urlPattern = "^test/(?<name>\\w+)/(?<id>\\d+)$")
@RenderString
public String foo(@Param("name") String name, @Param("id") int id) {
    return "name = " + name + ", id = " + id;
}
```

Or sometimes you just want to render a static html file.
```Java
@View(urlPattern = "^$")
@RenderHtml
public String index(){
    return "index.html";
}
```

With Xuntemplate you can render a template. (lack of demo)

If you don't want to use high-level API, you can just use HttpRequest and HttpResponse as if you are using Servlet.
```Java
@View(urlPattern = "^test$")
public void foo(Request req, Response res) {
    res.renderString("<h1>Hello!</h1>");
}
```

## Compilation
Clone all the source code into your local disk then use maven to compile and install xuncore.

## Demo
Use maven to compile and execute the demo.

```bash
mvn install exec:java
```

## Documents
This part is still under construction and I will write a full document to illustrate APIs and annotations. For quick viewing, please refer Feature section.
