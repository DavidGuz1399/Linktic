import { mount } from "@vue/test-utils";
import { describe, it, expect } from "vitest";
import ProductFormComponent from "../ProductFormComponent.vue";

describe("ProductFormComponent", () => {
  it('debe mostrar "Crear Nuevo Producto" cuando no se proporciona un producto', () => {
    const wrapper = mount(ProductFormComponent);
    expect(wrapper.text()).toContain("Crear Nuevo Producto");
  });

  it('debe mostrar "Editar Producto" cuando se proporciona un producto', () => {
    const wrapper = mount(ProductFormComponent, {
      props: { isEditing: true },
    });
    expect(wrapper.find("h3").text()).toBe("Editar Producto");
  });
});
