export class JwtAuth {
    
        accessToken: string;
        refreshToken: string;
        error?: string;
        expiresIn: number;
      
        constructor(
          accessToken: string = '',
          refreshToken: string = '',
          error?: string,
          expiresIn: number = 0
        ) {
          this.accessToken = accessToken;
          this.refreshToken = refreshToken;
          this.error = error;
          this.expiresIn = expiresIn;
        }
      
        static fromJson(json: any): JwtAuth {
          return new JwtAuth(
            json['access_token'],
            json['refresh_token'],
            json['error_description'],
            json['expires_in']
          );
        }
      
        hasError(): boolean {
          return this.error !== undefined;
        }
      }
      

