import { ComponentFixture, TestBed } from '@angular/core/testing';
import { HttpResponse, provideHttpClient } from '@angular/common/http';
import { FormBuilder } from '@angular/forms';
import { ActivatedRoute } from '@angular/router';
import { Subject, from, of } from 'rxjs';

import { IngestRunService } from '../service/ingest-run.service';
import { IIngestRun } from '../ingest-run.model';
import { IngestRunFormService } from './ingest-run-form.service';

import { IngestRunUpdateComponent } from './ingest-run-update.component';

describe('IngestRun Management Update Component', () => {
  let comp: IngestRunUpdateComponent;
  let fixture: ComponentFixture<IngestRunUpdateComponent>;
  let activatedRoute: ActivatedRoute;
  let ingestRunFormService: IngestRunFormService;
  let ingestRunService: IngestRunService;

  beforeEach(() => {
    TestBed.configureTestingModule({
      imports: [IngestRunUpdateComponent],
      providers: [
        provideHttpClient(),
        FormBuilder,
        {
          provide: ActivatedRoute,
          useValue: {
            params: from([{}]),
          },
        },
      ],
    })
      .overrideTemplate(IngestRunUpdateComponent, '')
      .compileComponents();

    fixture = TestBed.createComponent(IngestRunUpdateComponent);
    activatedRoute = TestBed.inject(ActivatedRoute);
    ingestRunFormService = TestBed.inject(IngestRunFormService);
    ingestRunService = TestBed.inject(IngestRunService);

    comp = fixture.componentInstance;
  });

  describe('ngOnInit', () => {
    it('should update editForm', () => {
      const ingestRun: IIngestRun = { id: 26980 };

      activatedRoute.data = of({ ingestRun });
      comp.ngOnInit();

      expect(comp.ingestRun).toEqual(ingestRun);
    });
  });

  describe('save', () => {
    it('should call update service on save for existing entity', () => {
      // GIVEN
      const saveSubject = new Subject<HttpResponse<IIngestRun>>();
      const ingestRun = { id: 26594 };
      jest.spyOn(ingestRunFormService, 'getIngestRun').mockReturnValue(ingestRun);
      jest.spyOn(ingestRunService, 'update').mockReturnValue(saveSubject);
      jest.spyOn(comp, 'previousState');
      activatedRoute.data = of({ ingestRun });
      comp.ngOnInit();

      // WHEN
      comp.save();
      expect(comp.isSaving).toEqual(true);
      saveSubject.next(new HttpResponse({ body: ingestRun }));
      saveSubject.complete();

      // THEN
      expect(ingestRunFormService.getIngestRun).toHaveBeenCalled();
      expect(comp.previousState).toHaveBeenCalled();
      expect(ingestRunService.update).toHaveBeenCalledWith(expect.objectContaining(ingestRun));
      expect(comp.isSaving).toEqual(false);
    });

    it('should call create service on save for new entity', () => {
      // GIVEN
      const saveSubject = new Subject<HttpResponse<IIngestRun>>();
      const ingestRun = { id: 26594 };
      jest.spyOn(ingestRunFormService, 'getIngestRun').mockReturnValue({ id: null });
      jest.spyOn(ingestRunService, 'create').mockReturnValue(saveSubject);
      jest.spyOn(comp, 'previousState');
      activatedRoute.data = of({ ingestRun: null });
      comp.ngOnInit();

      // WHEN
      comp.save();
      expect(comp.isSaving).toEqual(true);
      saveSubject.next(new HttpResponse({ body: ingestRun }));
      saveSubject.complete();

      // THEN
      expect(ingestRunFormService.getIngestRun).toHaveBeenCalled();
      expect(ingestRunService.create).toHaveBeenCalled();
      expect(comp.isSaving).toEqual(false);
      expect(comp.previousState).toHaveBeenCalled();
    });

    it('should set isSaving to false on error', () => {
      // GIVEN
      const saveSubject = new Subject<HttpResponse<IIngestRun>>();
      const ingestRun = { id: 26594 };
      jest.spyOn(ingestRunService, 'update').mockReturnValue(saveSubject);
      jest.spyOn(comp, 'previousState');
      activatedRoute.data = of({ ingestRun });
      comp.ngOnInit();

      // WHEN
      comp.save();
      expect(comp.isSaving).toEqual(true);
      saveSubject.error('This is an error!');

      // THEN
      expect(ingestRunService.update).toHaveBeenCalled();
      expect(comp.isSaving).toEqual(false);
      expect(comp.previousState).not.toHaveBeenCalled();
    });
  });
});
