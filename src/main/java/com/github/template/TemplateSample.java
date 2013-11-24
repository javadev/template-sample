package com.github.template;

import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStreamWriter;
import java.io.Writer;
import java.util.ArrayList;
import java.util.Properties;
import java.util.logging.Level;
import java.util.logging.Logger;

import org.apache.commons.io.IOUtils;
import org.apache.velocity.Template;
import org.apache.velocity.VelocityContext;
import org.apache.velocity.app.Velocity;
import org.apache.velocity.runtime.resource.loader.StringResourceLoader;
import org.apache.velocity.runtime.resource.util.StringResourceRepository;

public class TemplateSample {
    /** Instance logger */
    private Logger log;

    /**
     * By default, return a <code>SystemStreamLog</code> logger.
     *
     * @see org.apache.maven.plugin.Mojo#getLog()
     * @return the logger
     */
    public Logger getLog() {
        if (log == null) {
            log = Logger.getLogger(TemplateSample.class.getName());
        }
        return log;
    }

    /**
     * Get a template from the cache; if template has not been loaded, load it
     *
     * @param templatePath
     * @return
     */
    private static Template getTemplate(final String templatePath) {
        if (!Velocity.resourceExists(templatePath)) {
            StringResourceRepository repo = StringResourceLoader.getRepository();
            repo.putStringResource(templatePath, getTemplateFromResource(templatePath));
        }
        return Velocity.getTemplate(templatePath);
    }

    /**
     * Read a template into memory
     *
     * @param templatePath
     * @return
     */
    private static String getTemplateFromResource(final String templatePath) {
        try {
            InputStream stream = Thread.currentThread().getContextClassLoader().getResourceAsStream(templatePath);
            return IOUtils.toString(stream, "UTF-8");
        } catch (IOException ex) {
            throw new RuntimeException(ex);
        }
    }

    public static void main(String[] args) {
        Properties p = new Properties();
        p.setProperty("resource.loader", "string");
        p.setProperty("resource.loader.class", "org.apache.velocity.runtime.resource.loader.StringResourceLoader");
        Velocity.init(p);

        TemplateSample self = new TemplateSample();
        Template template = self.getTemplate("com/github/template/sample.vm");
        VelocityContext context = new VelocityContext();
        ArrayList<String> list = new ArrayList<String>();
        list.add("1");
        list.add("2");
        list.add("3");
        list.add("4");
        list.add("5");
        list.add("6");
        list.add("7");
        list.add("8");
        list.add("9");
        list.add("10");
        context.put("columns", list);
        try {
            Writer writer = new OutputStreamWriter(new FileOutputStream("result.txt"), "utf-8");
            template.merge(context, writer);
            writer.flush();
            writer.close();
        } catch (IOException ex) {
            self.getLog().log(Level.WARNING, ex.getMessage(), ex);
        }
    }
}
