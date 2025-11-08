import { ComponentFixture, TestBed } from '@angular/core/testing';
import { provideRouter, withComponentInputBinding } from '@angular/router';
import { RouterTestingHarness } from '@angular/router/testing';
import { of } from 'rxjs';

import { IngestRunDetailComponent } from './ingest-run-detail.component';

describe('IngestRun Management Detail Component', () => {
  let comp: IngestRunDetailComponent;
  let fixture: ComponentFixture<IngestRunDetailComponent>;

  beforeEach(async () => {
    await TestBed.configureTestingModule({
      imports: [IngestRunDetailComponent],
      providers: [
        provideRouter(
          [
            {
              path: '**',
              loadComponent: () => import('./ingest-run-detail.component').then(m => m.IngestRunDetailComponent),
              resolve: { ingestRun: () => of({ id: 26594 }) },
            },
          ],
          withComponentInputBinding(),
        ),
      ],
    })
      .overrideTemplate(IngestRunDetailComponent, '')
      .compileComponents();
  });

  beforeEach(() => {
    fixture = TestBed.createComponent(IngestRunDetailComponent);
    comp = fixture.componentInstance;
  });

  describe('OnInit', () => {
    it('should load ingestRun on init', async () => {
      const harness = await RouterTestingHarness.create();
      const instance = await harness.navigateByUrl('/', IngestRunDetailComponent);

      // THEN
      expect(instance.ingestRun()).toEqual(expect.objectContaining({ id: 26594 }));
    });
  });

  describe('PreviousState', () => {
    it('should navigate to previous state', () => {
      jest.spyOn(window.history, 'back');
      comp.previousState();
      expect(window.history.back).toHaveBeenCalled();
    });
  });
});
