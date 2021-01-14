//An Interface is a specification that identifies a related set of
//properties and methods to be implemented by a class. Concept in OOP but used in typescript
export interface LoginResponse {
    authenticationToken: string;
    refreshToken: string;
    expiresAt: Date;
    username: string;
}
