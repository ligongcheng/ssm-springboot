package cn.it.ssm.config;

import com.github.botaruibo.xvcode.generator.Gif2VCGenerator;
import com.github.botaruibo.xvcode.generator.Gif3VCGenerator;
import com.github.botaruibo.xvcode.generator.GifVCGenerator;
import com.github.botaruibo.xvcode.generator.PngVCGenerator;

public class CaptchaFactory {
    private Integer width;

    private Integer height;

    private Integer len;

    public CaptchaFactory(Integer width, Integer height, Integer len) {
        this.width = width;
        this.height = height;
        this.len = len;
    }

    public PngVCGenerator pngVCGenerator(){
        return new PngVCGenerator(width, height, len);
    }

    public GifVCGenerator gifVCGenerator(){
        return new GifVCGenerator(width,height,len);
    }

    public Gif2VCGenerator gif2VCGenerator(){
        return new Gif2VCGenerator(width,height,len);
    }

    public Gif3VCGenerator gif3VCGenerator(){
        return new Gif3VCGenerator(width,height,len);
    }

}
