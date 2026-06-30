import { AbstractControl, ValidationErrors, ValidatorFn } from "@angular/forms";

export function strongPasswordValidator(): ValidatorFn {
    return ( control: AbstractControl ) : ValidationErrors | null => {
        const value: string = control.value || '';
        const errors: ValidationErrors = {};

        if (value.length < 8) {
            errors['minLength'] = true;
        }
        if (!/[0-9]/.test(value)) {
            errors['noDigit'] = true;
        }
        if (!/[a-z]/.test(value)) {
            errors['noLowercase'] = true;
        }
        if (!/[A-Z]/.test(value)) {
            errors['noUppercase'] = true;
        }
        if (!/[^a-zA-Z0-9]/.test(value)) {
            errors['noSpecialChar'] = true;
        }

        return Object.keys(errors).length > 0 ? errors : null;
    }
}