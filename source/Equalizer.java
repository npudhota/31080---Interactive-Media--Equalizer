import processing.core.*; 
import processing.data.*; 
import processing.event.*; 
import processing.opengl.*; 

import processing.opengl.*; 
import ddf.minim.analysis.*; 
import ddf.minim.ugens.*; 
import ddf.minim.spi.*; 
import ddf.minim.*; 
import ddf.minim.effects.*; 
import ddf.minim.signals.*; 
import controlP5.*; 

import java.util.HashMap; 
import java.util.ArrayList; 
import java.io.File; 
import java.io.BufferedReader; 
import java.io.PrintWriter; 
import java.io.InputStream; 
import java.io.OutputStream; 
import java.io.IOException; 

public class Equalizer extends PApplet {

// Equalizer - V2.3
// Controls:
// Chords - A, S, D, F, V
// Notes - N, J, K, L, ;
// Music Play/Pause - Q
// Synthesizer - P(Activate), Any key(Deactivate)








 
Minim minim;
Sampler l1;
Sampler l2;
Sampler l3;
Sampler l4;
Sampler l5;
Sampler r1;
Sampler r2;
Sampler r3;
Sampler r4;
Sampler r5;
AudioPlayer jingle;
ddf.minim.analysis.FFT fft;
AudioInput in;
AudioOutput out;
FilePlayer song0;
ControlP5 controlP5;
ControlP5 controlP6;
ControlP5 controlP7;
String song01 = "song0.mp3";
String song02 = "song1.mp3";
String currentSong = song01;
AudioPlayer[] player=new AudioPlayer[3];
float[] angle;
float[] y, x;
PFont f;
int controlcolour = 0;
int colourstate = 0;
int i = 0;
// Ryan
LowPassFS lowPassFilter;
boolean lowPassFilterState = false;
int lowPassValue = 50;
char lastKey;

public void setup()
{
  
  minim = new Minim(this);
  out = minim.getLineOut();
  // Ryan
  song0 = new FilePlayer( minim.loadFileStream(song01));
  lowPassFilter = new LowPassFS(100, out.sampleRate());
  fft = new ddf.minim.analysis.FFT(out.bufferSize(), out.sampleRate());
  y = new float[fft.specSize()];
  x = new float[fft.specSize()];
  angle = new float[fft.specSize()];
  controlP5 = new ControlP5(this);
  controlP6 = new ControlP5(this);
  controlP7 = new ControlP5(this);
  controlP5.addButton("Controls", 1, (width/2)+((width/2)/2)+(height/3)-(height/30), (height/2)+((width/2)/2)-(height/40), 60, 20);
  controlP5.addButton("Filter On", 1, ((height/20)*2), (height/2)+((width/2)/2)-(height/40), 60, 20);
  controlP5.addButton("Filter Off", 1, ((height/20)*2), (width/2)+(height/15), 60, 20);
  frameRate(240);

  l1 = new Sampler ("l1.mp3", 4, minim);
  l2 = new Sampler ("l2.mp3", 4, minim);
  l3 = new Sampler ("l3.mp3", 4, minim);
  l4 = new Sampler ("l4.mp3", 4, minim);

  l5 = new Sampler ("l5.mp3", 4, minim);
  r1 = new Sampler ("r1.mp3", 4, minim);
  r2 = new Sampler ("r2.mp3", 4, minim);
  r3 = new Sampler ("r3.mp3", 4, minim);
  r4 = new Sampler ("r4.mp3", 4, minim);
  r5 = new Sampler ("r5.mp3", 4, minim);

  l1.patch(out);
  l2.patch(out);
  l3.patch(out);
  l4.patch(out);
  l5.patch(out);
  r1.patch(out);
  r2.patch(out);
  r3.patch(out);
  r4.patch(out);
  r5.patch(out);
  song0.patch(out);
  f = createFont("Arial", 16, true);
}

public void draw()
{
  background(0);
  textFont(f, 16);
  fill(color(controlcolour));
  text("Chords: A, S, D, F, V", (width/2)+((width/2)/2)+(height/3)-(height/12), (height/2)+((width/2)/2)-(height/8));
  text("Notes: N, J, K, L, ;", (width/2)+((width/2)/2)+(height/3)-(height/12), (height/2)+((width/2)/2)-(height/10));
  text("Play/Pause Music: Q", (width/2)+((width/2)/2)+(height/3)-(height/12), (height/2)+((width/2)/2)-(height/13));
  text("Synthesizer: P", (width/2)+((width/2)/2)+(height/3)-(height/12), (height/2)+((width/2)/2)-(height/20));

  fft.forward(out.mix);
  doubleAtomicSprocket();
  float note = random(1000);
  if (frameCount % 60 == 0) {
    if (key == 'p') {
      out.playNote(note);
    }
  }
}

public void controlEvent(ControlEvent theEvent) {
  if (theEvent.isController()) {
    if (theEvent.getController().getName()=="Controls") {
      if (colourstate == 0) {  
        controlcolour = 255;
        colourstate = 1;
      } else {
        controlcolour = 0;
        colourstate = 0;
      }
    }
  }
  if (theEvent.isController()) {
    if (theEvent.getController().getName()=="Filter On") {
      song0.unpatch(out);
      song0.patch(lowPassFilter);
      song0.patch(out);
    }
  }
  if (theEvent.isController()) {
    if (theEvent.getController().getName()=="Filter Off") {
      song0.unpatch(out);
      song0.unpatch(lowPassFilter);
      song0.patch(out);
    }
  }
}

public void keyPressed() {
  println("keyPressed: " + key);
  if (key == 'a') {
    l1.trigger();
  }
  if (key == 's') {
    l2.trigger();
  }
  if (key == 'd') {
    l3.trigger();
  }
  if (key == 'f') {
    l4.trigger();
  }
  if (key == 'v') {
    l5.trigger();
  }
  if (key == 'n') {
    r1.trigger();
  }
  if (key == 'j') {
    r2.trigger();
  }
  if (key == 'k') {
    r3.trigger();
  }
  if (key == 'l') {
    r4.trigger();
  }
  if (key == ';') {
    r5.trigger();
  }
  if (key == 'q') {
    if ( song0.isPlaying()) {
      song0.pause();
    } else {
      song0.play();
    }
  }
}

public void doubleAtomicSprocket() {
  noStroke();
  pushMatrix();
  translate(width/2, height/2);
  for (int i = 0; i < fft.specSize(); i++) {
    y[i] = y[i] + fft.getBand(i)/100;
    x[i] = x[i] + fft.getFreq(i)/100;
    angle[i] = angle[i] + fft.getFreq(i)/7500;
    rotateX(sin(angle[i]/2));
    rotateY(cos(angle[i]/2));
    fill(random(256), random(256), random(256)); // ring colour variable
    pushMatrix();
    translate((x[i]+50)%width/3, (y[i]+50)%height/3);
    box(fft.getBand(i)/20+fft.getFreq(i)/15);
    popMatrix();
  }
  popMatrix();
  pushMatrix();
  translate(width/2, height/2, 0);
  for (int i = 0; i < fft.specSize(); i++) {
    y[i] = y[i] + fft.getBand(i)/1000;
    x[i] = x[i] + fft.getFreq(i)/1000;
    angle[i] = angle[i] + fft.getFreq(i)/7500;
    rotateX(sin(angle[i]/2));
    rotateY(cos(angle[i]/2));
    fill(random(256), random(256), random(256)); // ring colour variable
    pushMatrix();
    translate((x[i]+250)%width, (y[i]+250)%height);
    box(fft.getBand(i)/20+fft.getFreq(i)/15);
    popMatrix();
  }
  popMatrix();
}
public void stop()
{
  jingle.close();
  minim.stop();
  super.stop();
}
  public void settings() {  size(1600, 900, P3D); }
  static public void main(String[] passedArgs) {
    String[] appletArgs = new String[] { "--present", "--window-color=#212020", "--stop-color=#cccccc", "Equalizer" };
    if (passedArgs != null) {
      PApplet.main(concat(appletArgs, passedArgs));
    } else {
      PApplet.main(appletArgs);
    }
  }
}
