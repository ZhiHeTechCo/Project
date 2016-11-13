package zh.co.common.ibatis;

import java.io.FilterInputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.PushbackInputStream;

public class BomRemoverInputStream extends FilterInputStream {

	private final char beginner = '<';

	public BomRemoverInputStream(InputStream in) throws IOException {
		super(new PushbackInputStream(in));
		while (this.in.read() != beginner) {
			// do nothing
		}
		((PushbackInputStream) this.in).unread(beginner);
	}

}
