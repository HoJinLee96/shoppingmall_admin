package net.chamman.shoppingmall_admin.exception;

import lombok.Getter;

@Getter
public abstract class CustomException extends RuntimeException{
	
	private HttpStatusCode httpStatusCode;
  
    protected CustomException(HttpStatusCode httpStatusCode, String message, Exception e) {
      super(message, e);
      this.httpStatusCode = httpStatusCode;
    }
    
    protected CustomException(HttpStatusCode httpStatusCode, String message) {
      super(message);
      this.httpStatusCode = httpStatusCode;
    }
    
    protected CustomException(HttpStatusCode httpStatusCode, Exception e) {
      super(e);
      this.httpStatusCode = httpStatusCode;
    }

    protected CustomException(HttpStatusCode httpStatusCode) {
        super();
        this.httpStatusCode = httpStatusCode;
    }
    
}
