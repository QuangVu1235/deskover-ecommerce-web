import {AfterViewInit, Component, OnDestroy, OnInit, ViewChild} from '@angular/core';
import {Discount} from "@/entites/discount";
import {Subject} from "rxjs";
import {DataTableDirective} from "angular-datatables";
import {NgbModal, NgbModalConfig} from "@ng-bootstrap/ng-bootstrap";
import {DiscountService} from "@services/discount.service";
import {DatePipe} from "@angular/common";
import {AlertUtils} from "@/utils/alert-utils";
import {BsDatepickerConfig} from "ngx-bootstrap/datepicker";
import {ProductService} from "@services/product.service";
import {Product} from "@/entites/product";

@Component({
  selector: 'app-promotion',
  templateUrl: './promotion.component.html',
  styleUrls: ['./promotion.component.scss']
})
export class PromotionComponent implements OnInit, AfterViewInit {
  discounts: Discount[];
  discount: Discount = <Discount>{};
  products: Product[];
  product: Product = <Product>{};

  isEdit: boolean = false;
  isActive: boolean = true;

  dtTrigger: Subject<any> = new Subject();
  dtOptions: any = {};
  dtAllProductOptions: any = {};
  dtDiscountProductOptions: any = {};

  bsConfig?: Partial<BsDatepickerConfig>;
  discountDateRange: Date[] = [new Date(), new Date()];
  discountStartTime: Date = new Date();
  discountEndTime: Date = new Date();

  @ViewChild('discountModal') discountModal: any;
  @ViewChild('productModal') productModal: any;
  @ViewChild(DataTableDirective, {static: false}) dtElement: DataTableDirective;

  constructor(
    private modalConfig: NgbModalConfig,
    private modalService: NgbModal,
    private discountService: DiscountService,
    private productService: ProductService,
  ) {
    modalConfig.size = 'lg';
    modalConfig.backdrop = 'static';
    modalConfig.keyboard = false;
    modalConfig.centered = true;

    // Config datepicker ngx-bootstrap
    this.bsConfig = Object.assign({}, {
      containerClass: 'theme-dark-blue',
      withTimepicker: false,
      locale: 'vi',
      rangeInputFormat: 'DD/MM/YYYY',
      dateInputFormat: 'DD/MM/YYYY',
      adaptivePosition: true,
      minDate: new Date(),
    });
  }

  ngOnInit() {
    const self = this;

    self.dtOptions = {
      pagingType: 'full_numbers',
      language: {
        url: "//cdn.datatables.net/plug-ins/1.12.0/i18n/vi.json"
      },
      responsive: true,
      lengthMenu: [5, 10, 25, 50, 100],
      serverSide: true,
      processing: true,
      stateSave: true, // sau khi refresh sẽ giữ lại dữ liệu đã filter, sort và paginate
      ajax: (dataTablesParameters: any, callback) => {
        this.discountService.getByActiveForDatatable(dataTablesParameters, this.isActive).then(resp => {
          self.discounts = resp.data;
          callback({
            recordsTotal: resp.recordsTotal,
            recordsFiltered: resp.recordsFiltered,
            data: self.discounts
          });
        });
      },
      columns: [
        {title: 'Tên', data: 'name', className: 'align-middle', responsivePriority: 1},
        {title: 'Mô tả', data: 'description', className: 'align-middle'},
        {
          title: 'Mức giảm giá (%)', data: 'percent', className: 'align-middle text-start text-md-center',
          responsivePriority: 3,
          render(data, type, row, meta) {
            return `<span class="badge bg-danger">${data}</span>`;
          }
        },
        {
          title: 'Thời gian bắt đầu', data: 'startDate', className: 'align-middle text-start text-md-center',
          render: function (data, type, row) {
            return new DatePipe('en-US').transform(data, 'dd/MM/yyyy HH:mm:ss');
          }
        },
        {
          title: 'Thời gian kết thúc', data: 'endDate', className: 'align-middle text-start text-md-center',
          render: function (data, type, row) {
            return new DatePipe('en-US').transform(data, 'dd/MM/yyyy HH:mm:ss');
          }
        },
        {
          title: 'Công cụ',
          data: null,
          orderable: false,
          searchable: false,
          className: 'align-middle text-end',
          responsivePriority: 2,
          render: (data, type, full, meta) => {
            if (self.isActive) {
              return `
              <div class="d-flex justify-content-end align-items-center">
              <a href="javascript:void(0)" class="btn btn-product btn-sm bg-faded-warning me-2" data-id="${data.id}"
                    title="Sản phẩm" data-toggle="tooltip">
                    <i class="fa-solid fa-box text-warning"></i>
                </a>
                <a href="javascript:void(0)" class="btn btn-edit btn-sm bg-faded-info me-2" data-id="${data.id}"
                    title="Sửa" data-toggle="tooltip">
                    <i class="fa fa-pen-square text-info"></i>
                </a>
                <a href="javascript:void(0)" class="btn btn-delete btn-sm bg-faded-danger" data-id="${data.id}"
                    title="Xoá" data-toggle="tooltip">
                    <i class="fa fa-trash text-danger"></i>
                </a>
              </div>
            `;
            } else {
              return `
               <button type="button" class="btn btn-active btn-sm bg-success" data-id="${data.id}">Kích hoạt</button>`
            }
          }
        },
      ]
    };

    self.dtAllProductOptions = {
      pagingType: 'full_numbers',
      language: {
        url: "//cdn.datatables.net/plug-ins/1.12.0/i18n/vi.json"
      },
      responsive: true,
      paging: false,
      info: false,
      scrollX: false,
      scrollY: '50vh',
      scrollCollapse: true,
      serverSide: true,
      processing: true,
      columnDefs: [{
        "defaultContent": "",
        "targets": "_all",
      }],
      ajax: (dataTablesParameters: any, callback) => {
        this.productService.getByActiveForDatatable(dataTablesParameters, true, null).then(resp => {
          self.products = resp.data.filter(product => {
            return product.discount === null;
          });
          callback({
            recordsTotal: resp.recordsTotal,
            recordsFiltered: resp.recordsFiltered,
            data: self.products
          });
        });
      },
      columns: [
        {
          title: 'Tên',
          data: 'name',
          className: 'align-middle',
          responsivePriority: 0,
        },
        {
          title: 'Thương hiệu',
          data: 'brand.name',
          className: 'align-middle',
        },
        {
          title: 'Danh mục',
          data: 'subCategory.name',
          className: 'align-middle'
        },
        {
          title: 'Công cụ',
          data: null,
          orderable: false,
          searchable: false,
          className: 'align-middle text-start text-md-end',
          render: (data, type, full, meta) => {
            if (self.isActive) {
              return `
               <button type="button" class="btn btn-add-product btn-sm bg-faded-success text-success" data-id="${data.id}">
                  Thêm
               </button>`
            }
          },
          responsivePriority: 1
        },
      ]
    };

    self.dtDiscountProductOptions = {
      pagingType: 'full_numbers',
      language: {
        url: "//cdn.datatables.net/plug-ins/1.12.0/i18n/vi.json"
      },
      responsive: true,
      paging: false,
      info: false,
      scrollX: false,
      scrollY: '50vh',
      scrollCollapse: true,
      serverSide: true,
      processing: true,
      columnDefs: [{
        "defaultContent": "",
        "targets": "_all",
      }],
      ajax: (dataTablesParameters: any, callback) => {
        this.productService.getByActiveForDatatable(dataTablesParameters, true, null).then(resp => {
          self.products = resp.data.filter(product => {
            if (product.discount) {
              return product.discount.id === this.discount.id;
            }
          });
          callback({
            recordsTotal: resp.recordsTotal,
            recordsFiltered: resp.recordsFiltered,
            data: self.products
          });
        });
      },
      columns: [
        {
          title: 'Tên',
          data: 'name',
          className: 'align-middle',
          responsivePriority: 0,
        },
        {
          title: 'Thương hiệu',
          data: 'brand.name',
          className: 'align-middle',
        },
        {
          title: 'Danh mục',
          data: 'subCategory.name',
          className: 'align-middle'
        },
        {
          title: 'Công cụ',
          data: null,
          orderable: false,
          searchable: false,
          className: 'align-middle text-start text-md-end',
          render: (data, type, full, meta) => {
            if (self.isActive) {
              return `
               <button type="button" class="btn btn-remove-product btn-sm bg-faded-danger text-danger" data-id="${data.id}">
                Xoá
               </button>`
            }
          },
          responsivePriority: 1
        },
      ]
    };
  }


  ngAfterViewInit() {
    const self = this;

    let body = $('body');
    body.on('click', '.btn-edit', function () {
      const id = $(this).data('id');
      self.editDiscount(id);
    });
    body.on('click', '.btn-delete', function () {
      const id = $(this).data('id');
      self.deleteDiscount(id);
    });
    body.on('click', '.btn-active', function () {
      const id = $(this).data('id');
      self.activeDiscount(id);
    });

    body.on('click', '.btn-product', function () {
      const id = $(this).data('id');
      self.getProduct(id);
    });

    body.on('click', '.btn-add-product', function () {
      const id = $(this).data('id');
      self.addProduct(id);
    });

    body.on('click', '.btn-remove-product', function () {
      const id = $(this).data('id');
      self.removeProduct(id);
    });

  }

  rerender() {
    this.dtElement.dtInstance.then((dtInstance: DataTables.Api) => {
      dtInstance.ajax.reload(null, false);
    });
  }

  filter() {
    this.dtElement.dtInstance.then((dtInstance: DataTables.Api) => {
      dtInstance.draw();
    });
  }

  newDiscount() {
    this.isEdit = false;
    this.discount = <Discount>{};

    this.discountDateRange = [new Date(), new Date()];
    this.discountStartTime = new Date();
    this.discountEndTime = new Date();

    this.openModal(this.discountModal);
  }

  getDiscount(discountId: number) {
    this.discountService.getById(discountId).subscribe(data => {
      this.discount = data;
      this.discountDateRange = [new Date(this.discount.startDate), new Date(this.discount.endDate)];
      this.discountStartTime = new Date(this.discount.startDate);
      this.discountEndTime = new Date(this.discount.endDate);
    });
  }

  editDiscount(discountId: number) {
    this.isEdit = true;
    this.getDiscount(discountId);
    this.openModal(this.discountModal);
  }

  saveDiscount(discount: Discount) {
    this.discount.startDate = this.discountDateRange[0].setTime(this.discountStartTime.getTime());
    this.discount.endDate = this.discountDateRange[1].setTime(this.discountEndTime.getTime());
    if (this.isEdit) {
      this.discountService.update(discount).subscribe(data => {
        AlertUtils.toastSuccess('Cập nhật thành công');
      }, error => {
        AlertUtils.toastError(error);
      });
    } else {
      this.discountService.create(discount).subscribe(data => {
        AlertUtils.toastSuccess('Thêm mới thành công');
      }, error => {
        AlertUtils.toastError(error);
      });
    }
    this.rerender();
    this.closeModal();
  }

  deleteDiscount(discountId: number) {
    AlertUtils.warning('Xác nhận xoá', 'Khuyến mãi đang áp dụng trên sản phẩm sẽ bị huỷ').then((result) => {
      if (result.value) {
        this.discountService.changeActive(discountId).subscribe(data => {
          AlertUtils.toastSuccess('Xoá danh mục thành công');
          this.rerender();
        });
      }
    });
  }

  activeDiscount(discountId: number) {
    this.discountService.changeActive(discountId).subscribe(data => {
      AlertUtils.toastSuccess('Kích hoạt danh mục thành công');
      this.rerender();
    });
  }

  getProduct(discountId: number) {
    this.getDiscount(discountId);
    this.openModal(this.productModal, 'xl');
  }

  addProduct(productId: number) {
    this.discountService.update(this.discount, productId, null).subscribe(data => {
      AlertUtils.toastSuccess('Thêm sản phẩm thành công');
      $('.product-table').DataTable().ajax.reload(null, false);

    });
  }

  removeProduct(productId: number) {
    this.discountService.update(this.discount, null, productId).subscribe(data => {
      AlertUtils.toastSuccess('Xoá sản phẩm thành công');
      $('.product-table').DataTable().ajax.reload(null, false);
    });
  }

  // Modal
  openModal(content: any, size: string = 'lg') {
    this.closeModal();
    this.modalConfig.size = size;
    this.modalService.open(content);
  }

  closeModal() {
    this.modalService.dismissAll();
  }
}
