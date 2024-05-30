import { Component, OnInit } from '@angular/core';
import { AuthService } from '../../services/auth/auth.service';
import { Auth } from '../../intefaces/auth/auth';
import { FormBuilder, FormGroup, ReactiveFormsModule, Validators } from '@angular/forms';
import { NgbCarouselModule } from '@ng-bootstrap/ng-bootstrap';
import { TranslocoModule, TranslocoService } from '@ngneat/transloco';
import Swal from 'sweetalert2';
import { Router } from '@angular/router';
import { HeaderComponent } from '../common/header/header.component';

@Component({
  selector: 'app-login',
  standalone: true,
  imports: [ReactiveFormsModule, NgbCarouselModule, TranslocoModule, HeaderComponent],
  templateUrl: './login.component.html',
  styleUrl: './login.component.css'
})
export class LoginComponent implements OnInit {

  auth: Auth;
  form: FormGroup;
  submitted = false;

  constructor(
    private authService: AuthService,
    private translocoService: TranslocoService,
    private formBuilder: FormBuilder,
    private router: Router
  ) {
    this.translocoService.setActiveLang( 'es' );
  }

  ngOnInit(): void {
    sessionStorage.clear();
    this.form = this.formBuilder.group({
      user      : ['', Validators.required],
      password  : ['', Validators.required],
      branch    : [1, Validators.required]
    });
  }

  onSubmit() {
    this.submitted = true;

    if (!this.form.valid) { return; }

    this.auth = this.form.value;

    Swal.fire({
      html:  this.translocoService.translate('0005'),
      icon: 'info',
      allowOutsideClick: false
    })

    Swal.showLoading();

    this.authService.login( this.auth ).subscribe({
      next: (result) => {
        Swal.close();
        this.submitted = false;
        this.router.navigate(['/cube']);
      },
      error: (err) => {
        Swal.close();
        this.submitted = false;
        Swal.fire({
          icon: 'error',
          title: 'Error',
          text: this.translocoService.translate('0006')
        });
      },
    });
  }
}
