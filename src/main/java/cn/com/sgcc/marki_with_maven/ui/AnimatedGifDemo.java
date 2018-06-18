package cn.com.sgcc.marki_with_maven.ui;

import java.awt.image.BufferedImage;
import java.net.URISyntaxException;

import javax.swing.JFrame;

import javafx.animation.Interpolator;
import javafx.animation.Transition;
import javafx.embed.swing.SwingFXUtils;
import javafx.scene.Scene;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.image.WritableImage;
import javafx.scene.layout.HBox;
import javafx.util.Duration;

/**
 * Requires GifDecoder from here: http://www.java2s.com/Code/Java/2D-Graphics-GUI/DecodesaGIFfileintooneormoreframes.htm
 */
@SuppressWarnings("restriction")
public class AnimatedGifDemo extends JFrame {

//	final JFXPanel fxPanel = new JFXPanel();
    
    public Scene createScene() throws URISyntaxException {

        HBox root = new HBox();

        // TODO: provide gif file, ie exchange banana.gif with your file
        Animation ani = new AnimatedGif("1.gif", 10000);
        ani.setCycleCount(1);
        ani.play();

        root.getChildren().addAll( ani.getView());

        Scene scene = new Scene(root, 1600, 900);
        return scene;
//        primaryStage.setScene(scene);
//        primaryStage.show();

    }

    public static void main(String[] args) {
//       new Stage
//    	Animation ani = new AnimatedGif("1.gif", 10000);
//        ani.setCycleCount(1);
//        ani.play();
//    	JFrame jFrame = new JFrame();
//    	jFrame.add(ani.getView());
    }

    public class AnimatedGif extends Animation {

        public AnimatedGif( String filename, double durationMs) {

            GifDecoder d = new GifDecoder();
            d.read( filename);

            Image[] sequence = new Image[ d.getFrameCount()];
            for( int i=0; i < d.getFrameCount(); i++) {

                WritableImage wimg = null;
                BufferedImage bimg = d.getFrame(i);
                sequence[i] = SwingFXUtils.toFXImage( bimg, wimg);

            }

            super.init( sequence, durationMs);
        }

    }

    public class Animation extends Transition {

        private ImageView imageView;
        private int count;

        private int lastIndex;

        private Image[] sequence;

        private Animation() {
        }

        public Animation( Image[] sequence, double durationMs) {
            init( sequence, durationMs);
        }

        private void init( Image[] sequence, double durationMs) {
            this.imageView = new ImageView(sequence[0]);
            this.sequence = sequence;
            this.count = sequence.length;

            setCycleCount(1);
            setCycleDuration(Duration.millis(durationMs));
            setInterpolator(Interpolator.LINEAR);

        }

        protected void interpolate(double k) {

            final int index = Math.min((int) Math.floor(k * count), count - 1);
            if (index != lastIndex) {
                imageView.setImage(sequence[index]);
                lastIndex = index;
            }

        }

        public ImageView getView() {
            return imageView;
        }

    }

}