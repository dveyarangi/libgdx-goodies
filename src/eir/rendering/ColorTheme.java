package eir.rendering;

import yarangi.numbers.RandomUtil;

import com.badlogic.gdx.graphics.Color;


public class ColorTheme
{
	int theme;
	
	float [] themes;

	
	public ColorTheme()
	{
		
	}
	
	public void setTheme(int theme) 
	{ 
		this.theme = theme; 
		
		this.themes = new float [] { theme, theme - 150, theme + 150 };
	}
	
	public Color hsv2rgb(final float h, float s, float v, Color color)
	{
	    float      hh, p, q, t, ff;
	    int        i;
	    float r, g, b;

	    if(s <= 0.0) {       // < is bogus, just shuts up warnings
	        r = v;
	        g = v;
	        b = v;
	        
	        color.set(r, g, b, 1f);
	        
	        return color;
	    }
	    if(s > 1) s = 1;
	    if(s < 0) s = 0;
	    if(v > 1) v = 1;
	    if(v < 0) v = 0;
	    hh = h % 360;
	    if(hh < 0) hh += 360;


	    hh /= 60.0;
	    i = (int)hh;
	    ff = hh - i;
	    p = v * (1.0f - s);
	    q = v * (1.0f - s * ff);
	    t = v * (1.0f - s * (1.0f - ff));

	    switch(i) {
	    case 0:
	        r = v;
	        g = t;
	        b = p;
	        break;
	    case 1:
	        r = q;
	        g = v;
	        b = p;
	        break;
	    case 2:
	        r = p;
	        g = v;
	        b = t;
	        break;

	    case 3:
	        r = p;
	        g = q;
	        b = v;
	        break;
	    case 4:
	        r = t;
	        g = p;
	        b = v;
	        break;
	    case 5:
	    default:
	        r = v;
	        g = p;
	        b = q;
	        break;
	    }
        color.set(r, g, b, 1f);
        
        return color;
	}

	public Color getEnemyColor()
	{
		float hue = RandomUtil.STD(themes[RandomUtil.N( themes.length )], 5);
		float sat = RandomUtil.R(0.2f)+0.75f;
		float val = 0.8f;
		return hsv2rgb(hue, sat, val, new Color());
	}
}
